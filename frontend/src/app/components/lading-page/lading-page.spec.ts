import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { LadingPage } from './lading-page';

describe('LadingPage', () => {
  let component: LadingPage;
  let fixture: ComponentFixture<LadingPage>;
  let host: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LadingPage],
      // RouterLink precisa do Router e do ActivatedRoute para gerar os hrefs
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(LadingPage);
    component = fixture.componentInstance;
    host = fixture.nativeElement as HTMLElement;

    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('header', () => {
    it('renderiza o logo no header e no footer', () => {
      expect(host.querySelectorAll('.logo').length).toBe(2);
      expect(host.querySelector('.header .logo-text')?.textContent).toContain('Rating');
      expect(host.querySelector('.footer .logo-text')?.textContent).toContain('Rating');
    });

    it('renderiza os três links de navegação', () => {
      const links = Array.from(host.querySelectorAll('.nav-link')).map((a) =>
        a.textContent?.trim(),
      );

      expect(links).toEqual(['Como funciona', 'Por que aprender', 'Relatar erros']);
    });

    it('o botão "Entrar" abre a tela de acesso no modo login', () => {
      const link = host.querySelector('.header-actions .btn-ghost') as HTMLAnchorElement;

      expect(link.textContent?.trim()).toBe('Entrar');
      expect(link.getAttribute('href')).toBe(`${component.rotaEntrar}?modo=login`);
    });

    it('o botão "Cadastre-se" abre a tela de acesso no modo cadastro', () => {
      const link = host.querySelector('.header-actions .btn-gold-sm') as HTMLAnchorElement;

      expect(link.textContent?.trim()).toBe('Cadastre-se');
      expect(link.getAttribute('href')).toBe(`${component.rotaCadastro}?modo=cadastro`);
    });
  });

  describe('conteúdo', () => {
    it('renderiza um card para cada benefício', () => {
      const cards = host.querySelectorAll('.feature-card');

      expect(cards.length).toBe(component.benefits.length);

      component.benefits.forEach((benefit, i) => {
        expect(cards[i].querySelector('.feature-number')?.textContent).toContain(benefit.number);
        expect(cards[i].querySelector('h3')?.textContent).toContain(benefit.title);
        expect(cards[i].querySelector('p')?.textContent).toContain(benefit.text);
      });
    });

    it('renderiza um ícone SVG em cada card de benefício', () => {
      const icons = host.querySelectorAll('.feature-card .feature-icon svg path');

      expect(icons.length).toBeGreaterThanOrEqual(component.benefits.length);
    });

    it('os botões de chamada para ação levam ao cadastro', () => {
      const ctas = [
        host.querySelector('.hero-buttons .btn-primary'),
        host.querySelector('.cta-section .btn-dark'),
      ] as HTMLAnchorElement[];

      ctas.forEach((cta) => {
        expect(cta.getAttribute('href')).toBe(`${component.rotaCadastro}?modo=cadastro`);
      });
    });

    it('o link de contato no footer usa o e-mail do componente', () => {
      const mail = host.querySelector('.footer-mail') as HTMLAnchorElement;

      expect(mail.getAttribute('href')).toBe(`mailto:${component.email}`);
      expect(mail.textContent?.trim()).toBe(component.email);
    });
  });

  describe('goTo', () => {
    // Stub manual (sem jasmine/vi) para funcionar em qualquer runner.
    // O jsdom não implementa scrollIntoView, então atribuímos direto no elemento.
    function stubScroll(id: string) {
      const el = document.getElementById(id) as HTMLElement;
      const calls: (boolean | ScrollIntoViewOptions | undefined)[] = [];
      el.scrollIntoView = (arg?: boolean | ScrollIntoViewOptions) => {
        calls.push(arg);
      };
      return calls;
    }

    it('cancela o comportamento padrão do link', () => {
      stubScroll('metodo');
      const event = new Event('click', { cancelable: true });

      component.goTo(event, 'metodo');

      expect(event.defaultPrevented).toBe(true);
    });

    it('rola suavemente até a seção informada', () => {
      const calls = stubScroll('metodo');

      component.goTo(new Event('click', { cancelable: true }), 'metodo');

      expect(calls).toEqual([{ behavior: 'smooth', block: 'start' }]);
    });

    it('não lança erro quando o id não existe', () => {
      const event = new Event('click', { cancelable: true });

      expect(() => component.goTo(event, 'id-inexistente')).not.toThrow();
      expect(event.defaultPrevented).toBe(true);
    });

    it('clicar em "Como funciona" rola até a seção #metodo', () => {
      const calls = stubScroll('metodo');
      const link = host.querySelector('.nav-link') as HTMLAnchorElement;

      link.click();

      expect(calls.length).toBe(1);
    });

    it('clicar no logo rola até a seção #inicio', () => {
      const calls = stubScroll('inicio');
      const logo = host.querySelector('.header .logo') as HTMLAnchorElement;

      logo.click();

      expect(calls.length).toBe(1);
    });
  });
});