ALTER TABLE subtopics ADD COLUMN content TEXT;


-- =============================================
-- Capítulo 1 — Introdução ao Xadrez
-- =============================================

UPDATE subtopics SET content = 'O xadrez é um dos jogos de tabuleiro mais populares e antigos do mundo, e possui como foco principal a estratégia. É disputado entre dois jogadores, frente a frente, e cada um joga em um tabuleiro quadrado dividido em 64 casas alternadas entre cores claras e escuras (tradicionalmente chamadas de casas brancas e pretas).

As peças do xadrez são divididas em 16 para cada jogador e são posicionadas em lados opostos do tabuleiro, sendo um lado para peças brancas e outro para peças pretas. Outro detalhe fundamental é que o jogo não possui dados nem cartas escondidas. Isso significa que o fator sorte não existe, e tudo que ocorre na partida está visível para ambos os jogadores.

As jogadas do xadrez acontecem em turnos alternados, começando sempre pelo jogador que conduz as peças brancas. Isso significa que cada jogador tem direito a um turno e, ao terminar a jogada, passa a vez para o adversário. Durante a partida, os jogadores precisam analisar possibilidades, planejar movimentos e antecipar as ações do oponente para obter vantagem no jogo.

Além de ser amplamente conhecido como uma forma de entretenimento, o xadrez também é reconhecido por melhorar a capacidade cognitiva, auxiliando assim na concentração, memória, paciência e tomada de decisões. Por conta disso, o xadrez é jogado mundialmente tanto de forma recreativa quanto competitiva.'
WHERE title = 'O que é o xadrez'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1);

UPDATE subtopics SET content = 'O objetivo definitivo do xadrez é dar xeque-mate no rei adversário, ou seja, criar uma situação em que o rei afetado não poderá escapar do movimento da peça inimiga.

Isso acontece quando o rei está sob ataque (em xeque), e não pode se movimentar para nenhuma casa segura, além da peça responsável pelo ataque não poder ser capturada ou bloqueada. Quando esses casos ocorrem, a partida é encerrada imediatamente, e o jogador responsável pelo xeque-mate vence a partida.

No xadrez, o rei é considerado a peça mais importante, pois o andamento da partida depende diretamente da situação do rei no tabuleiro. Por conta disso, o objetivo principal não é capturar todas as peças do adversário, mas sim criar uma situação na qual o rei inimigo não tenha nenhuma possibilidade de defesa.

É totalmente possível que um jogador com menos peças que o adversário no tabuleiro consiga vencer com um movimento altamente estratégico. Além disso, partidas também podem terminar de outras formas, como a desistência de um dos jogadores ou um empate, caso os dois jogadores fiquem sem peças suficientes para dar xeque-mate.'
WHERE title = 'Objetivo do jogo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1);

UPDATE subtopics SET content = 'As origens do conceito de xadrez datam de antes do século VI D.C, quando um jogo chamado Chaturanga se formou na região onde atualmente é a Índia. A palavra Chaturanga significa "quatro divisões", e as peças representavam símbolos militares do exército indiano na época, sendo esses símbolos: infantaria (que deu origem aos peões), cavalaria (cavalos), elefante (futuramente se tornando bispos), carruagem (atuais torres), general e rei.

O jogo já possuía características estratégicas semelhantes às do xadrez moderno, principalmente pela importância do posicionamento das peças e do planejamento de cada jogada. Da região da Índia, o Chaturanga foi introduzido ao território da Pérsia (atual Irã), onde passou a ser chamado de Shatranj. Foi na Pérsia que surgiu a expressão "Shah Mat", que em persa pode ser traduzido como "O Rei está indefeso", dando origem ao termo xeque-mate, usado no xadrez atual.

Com a expansão das rotas comerciais e avanços territoriais, o formato do jogo se espalhou pelo continente asiático e posteriormente para a Europa com o passar dos séculos e, com o tempo, as regras e as peças passaram por diversas reformulações, chegando aos padrões próximos do xadrez moderno no século XVI. Nessa época, vários estudiosos do xadrez, como Luis Ramírez de Lucena e Ruy Lopez de Segura, escreveram livros que influenciaram o desenvolvimento das estratégias e a organização das regras. Essas mudanças foram essenciais para consolidar o xadrez no formato conhecido nos dias de hoje.'
WHERE title = 'História do xadrez'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1);

UPDATE subtopics SET content = 'Apesar de sua longa história, nem sempre o xadrez foi considerado um esporte. Durante muitos séculos, o xadrez era visto apenas como um jogo de lazer e não havia muita padronização nas regras.

Entretanto, durante o século XIX, começou a se formar o padrão competitivo atual, que moldou o xadrez como um esporte, e impulsionou grandes mudanças para o xadrez, como a criação de relógios específicos para o controle de tempo do xadrez e a padronização das peças, além da criação de torneios esportivos.

Nesse mesmo período, vários jogadores se destacaram pela criação de novos estilos de estratégias e de jogadas, alguns deles sendo Paul Morphy, um prodígio americano que ficou conhecido por suas jogadas rápidas e eficientes baseadas no movimento das peças, e Adolf Anderssen, famoso pela "Partida Imortal", na qual fez uso espetacular dos sacrifícios de suas próprias peças maiores para dar xeque-mate no oponente.

Em 1924, o status esportivo do xadrez foi consolidado com a fundação da FIDE (Federação Internacional de Xadrez), em Paris, organizando o esporte em escala global. Atualmente, o xadrez é formalmente reconhecido pelo Comitê Olímpico Internacional (COI) como um esporte mental, exigindo dos competidores não apenas capacidade intelectual, como também um preparo psicológico e físico aprimorado para suportar partidas exaustivas.'
WHERE title = 'Como o xadrez se tornou esporte'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1);

UPDATE subtopics SET content = 'O Rating é o sistema matemático de pontuação do xadrez que indica o quão habilidoso é o jogador. Essa pontuação acompanha o jogador por toda a sua trajetória, e quanto maior o Rating, maior o nível de habilidade da pessoa. Esse sistema é bastante usado em competições para classificar os jogadores e criar partidas com jogadores de níveis parecidos, tornando assim as partidas mais justas.

Existem vários sistemas de pontuação no xadrez. O método mais tradicional e popular do mundo é o Elo, que foi adotado oficialmente pela FIDE (Federação Internacional de Xadrez). Esse método foi criado na década de 1960 por Arpad Elo, um professor americano de física e especialista no xadrez. O Elo atualiza o rating dos jogadores com base nos resultados das partidas e na diferença de nível entre os dois adversários.

O valor do Rating pode mudar dependendo do resultado da partida do jogador. Em caso de derrota, o jogador perde pontos, mas em caso de vitória, ganha pontos. Já no caso de um empate, os pontos são divididos de forma justa entre os adversários. A quantidade de pontos que irão mudar o rating também depende do nível do adversário. Por exemplo, se um jogador com um rating muito alto vence um iniciante, ele ganha poucos pontos, pois, estatisticamente falando, esse é o resultado esperado. Mas se o iniciante conseguir ganhar de alguém com rating alto, ele aumenta muito sua pontuação, enquanto o mestre perde uma quantidade significativa de pontos.

Para representar melhor a escala, um iniciante no sistema Elo costuma ter um rating entre 400 e 800 pontos, enquanto jogadores de clubes regulares variam entre 1200 e 1800 pontos. Quando um jogador ultrapassa a marca dos 2500 pontos no sistema da FIDE, ele atinge o nível necessário para disputar o título máximo do xadrez, chamado de Grande Mestre (GM). Um fato curioso é que o recorde histórico do sistema Elo pertence ao norueguês Magnus Carlsen, que atingiu 2882 pontos em 2014.

Com a popularidade do xadrez na internet, surgiu a necessidade de sistemas que correspondessem aos padrões modernos, sendo o principal deles o Sistema Glicko. Criado pelo professor Mark Glickman, o Glicko é usado com bastante frequência em plataformas online. A grande diferença do Glicko para o Elo é a adição do fator volatilidade e a "certeza" sobre a habilidade do jogador.

Por exemplo, se um jogador compete todos os dias, o sistema tem mais "certeza" do nível dele, e seus pontos variam pouco a cada partida. Entretanto, se o jogador ficar inativo por muitos meses, o sistema deduz que o nível dele pode ter mudado. Assim, quando ele voltar a jogar, as primeiras partidas farão seu rating subir ou descer de forma muito mais drástica, até que o sistema consiga recalibrar a força de jogo atual do jogador.'
WHERE title = 'O que é Rating'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1);

UPDATE subtopics SET content = 'Assim como qualquer área esportiva ou de estudos, o xadrez também possui uma linguagem própria. Para entender melhor como o xadrez funciona, acompanhar partidas e facilitar o significado de alguns elementos do xadrez, é importante o aprendizado de alguns termos técnicos que são usados com frequência no jogo:

Xeque: É o aviso padrão de perigo, que ocorre quando o rei está sob ataque, mas ainda havendo chance de escapar, bloquear ou capturar o atacante.
Xeque-mate: É o ataque final que ocorre quando o rei está sob ataque e não pode escapar.
Empate: Quando a partida termina sem vencedor.
Captura: Ocorre quando uma peça remove outra do tabuleiro. No caso do xadrez, a captura acontece quando a peça atacante ocupa o lugar da peça adversária, removendo-a da partida.

Os jogadores de xadrez também costumam dividir a partida em três fases principais, para facilitar a compreensão da mudança de comportamento durante o jogo.

Abertura: Fase inicial do jogo, na qual os jogadores tiram as peças de suas casas de origem e iniciam estratégias para o domínio do tabuleiro.
Meio-jogo: A fase intermediária e geralmente a mais complexa, com mais interações entre as peças, trocas de captura e táticas de ataque.
Final: Fase final da partida, em que a maioria das peças já foi capturada, o tabuleiro fica mais vazio e as peças que sobraram ganham mais poder de influência na partida.

Além disso, há alguns termos usados no decorrer de toda a partida.
Desenvolvimento: Significa o movimento das peças para posições mais ativas e úteis, para evitar peças ociosas na partida.
Centro: É a região central do tabuleiro, importante para o controle do jogo, devido ao excelente fluxo que oferece para as peças que ocupam esse espaço.'
WHERE title = 'Vocabulário básico do xadrez'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1);

UPDATE subtopics SET content = 'Também é interessante aprender a linguagem que os fãs de xadrez usam com bastante frequência de forma informal. Esses termos foram desenvolvidos por causa da popularização das transmissões ao vivo e dos clubes online.

Rei afogado: Quando o rei não está em xeque, mas também não consegue se movimentar para nenhum outro lugar, gerando o empate da partida.
Blunder: Erro muito grande (termo em inglês bastante usado).

Também é muito comum os jogadores utilizarem símbolos na linguagem do xadrez, isso acontece por conta que a escrita oficial do xadrez usa essas pontuações gráficas para avaliar jogadas. Informalmente, esses símbolos podem ser escritos como verbos ("!" vira exclamou, por exemplo).
Interrogação (?): Indica uma jogada ruim.
Capivarada/Erro grave (?!): Erro bobo/grave cometido por um jogador.
Exclamou uma (!): Lance forte, preciso, de boa qualidade.
Exclamou duas (!!): Lance brilhante (algo excepcional, difícil de encontrar).

Por fim, existem as gírias que indicam a temperatura psicológica do jogador na partida.
Tô perdido: Indica que o jogador está com uma desvantagem tão grande em relação ao adversário que está próximo da derrota.
Tô ganho: Indica que o jogador está com bastante vantagem no jogo, estando próximo da vitória.
Tô no jogo: O jogador ainda tem chances de empatar/ganhar o jogo, mesmo após perdas significativas na partida.'
WHERE title = 'Vocabulário popular do xadrez'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 1);

-- =============================================
-- Capítulo 2 — O Tabuleiro e as Peças
-- =============================================

UPDATE subtopics SET content = 'O tabuleiro é onde ocorre todo o andamento da partida. Ele tem um formato perfeitamente quadrado e é dividido em uma malha 8x8, totalizando 64 quadrados pequenos, que na linguagem do xadrez são chamados de casas.

Existe uma regra no xadrez, em que, para ambos os jogadores, a casa no canto inferior direito do tabuleiro precisa ser obrigatoriamente de cor clara. Após o alinhamento do tabuleiro, cada jogador irá preencher as duas primeiras fileiras voltadas para si com as 16 peças disponíveis para cada um, deixando as 4 fileiras restantes como um vão entre as peças, isso significa que as casas dessa região começam sem peças.'
WHERE title = 'Estrutura do tabuleiro'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2);

UPDATE subtopics SET content = 'As 64 casas do tabuleiro são divididas, de forma alternada, entre 32 quadrados de cor clara e 32 quadrados de cor escura, criando um padrão quadriculado bem definido.

Atualmente, os tabuleiros são fabricados em diversos tipos de materiais e tons de cores, como o clássico padrão de madeira em tons marrons, os tabuleiros de couro em preto e branco que podem ser enrolados e levado facilmente para qualquer lugar, além dos tabuleiros de plástico usados em torneios.

Contudo, independentemente do material, na linguagem oficial do xadrez as casas sempre são referidas como casas brancas e pretas ou claras e escuras. Esse padrão alternado não serve apenas para estética, também é útil para melhorar a noção de espaço do tabuleiro para cada jogador.'
WHERE title = 'Casas claras e escuras'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2);

UPDATE subtopics SET content = 'Para entender a movimentação do tabuleiro, é importante saber que o espaço do jogo é dividido visualmente em três tipos de direções possíveis, formadas por casas que se conectam criando caminhos específicos no tabuleiro.

Começando pela direção das colunas, elas são 8 linhas verticais, com 8 casas de extensão, que vão de um jogador ao outro. Portanto, a direção se estende do norte ao sul. As colunas são essenciais para a movimentação das peças em direção às peças adversárias.

Já as fileiras são 8 linhas horizontais que vão da direção leste a oeste do tabuleiro. Seriam como se fossem as "trincheiras" do jogo, pois, conforme as peças avançam, ficam cada vez mais próximas do ataque, mas se alguma peça recuar, isso pode ser considerado uma ação de defesa.

E por fim as diagonais, que são as linhas inclinadas formadas por casas que se tocam apenas pelas pontas. Todas as casas de uma linha diagonal são da mesma cor. Além disso, as diagonais podem ter tamanhos diferentes, quanto mais próxima a linha está do centro do tabuleiro, maior ela é, podendo chegar a até oito casas de comprimento, enquanto as diagonais próximas das pontas ficam cada vez menores, sendo duas casas o comprimento mínimo possível.'
WHERE title = 'Colunas, fileiras e diagonais'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2);

UPDATE subtopics SET content = 'Para os jogadores conseguirem se comunicar, estudar e registrar partidas de maneira mais simplificada e direta, foi necessário que cada casa do tabuleiro ganhasse uma identificação única, assim como em um plano cartesiano ou no jogo de "Batalha Naval". Esse sistema de nomeação é conhecido como Notação Algébrica.

A identificação de cada casa é formada pelo cruzamento do eixo horizontal e vertical. As casas no eixo horizontal são nomeadas, da esquerda à direita, de "a" até "h", já as casas no eixo vertical são nomeadas, de baixo para cima, de 1 até 8. Um detalhe especial é que a ordem dessa notação é sempre planejada a partir da perspectiva do jogador que conduz as peças brancas.

Como a casa é identificada pelo cruzamento dos eixos, a notação é criada pela junção do eixo das letras com o eixo dos números. A principal regra é que as letras sempre devem vir primeiro.

Por exemplo, uma casa que fica localizada na fileira "d" no eixo horizontal e cruza com a coluna "4" no eixo vertical tem como endereço "d4". Caso a casa fosse nomeada "4d", estaria errado e fora dos padrões do xadrez.'
WHERE title = 'Nomeação das casas'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2);

UPDATE subtopics SET content = 'Agora que o tabuleiro já é conhecido, é o momento ideal para apresentar as peças do jogo. Cada jogador tem direito a 16 peças, que são posicionadas de forma idêntica nas duas primeiras fileiras voltadas para si no tabuleiro.

Essas peças são divididas em seis tipos distintos, sendo elas:
• 1 rei
• 1 dama
• 2 bispos
• 2 torres
• 2 cavalos
• 8 peões

Cada tipo possui um estilo de movimento diferente, o que torna o jogo extremamente dinâmico. Os peões atuam como uma infantaria na linha de frente, já os cavalos, bispos e torres possuem importância tática durante a partida, sendo essenciais para ações de ataque e defesa.

Entretanto, as peças com as funções mais importantes do jogo são a Dama e o Rei. A dama é a peça com maior alcance e poder de movimento entre todas as outras, sendo a artilharia pesada do xadrez. Já o rei, apesar de possuir movimentos lentos e extremamente limitados, é a peça mais importante do jogo. Isso ocorre pois, como mencionado anteriormente, a partida imediatamente termina com a derrota do Rei. Sendo assim, todas as outras peças trabalham em conjunto para um único propósito: defender o próprio Rei enquanto tentam cercar o Rei adversário.'
WHERE title = 'Conhecendo as peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2);

UPDATE subtopics SET content = 'No xadrez, não existe placar de pontos para definir o vencedor, sendo o xeque-mate a única maneira de ganhar a partida. Entretanto, para fins matemáticos e estratégicos, as peças possuem valores simbólicos. Esse sistema funciona como um guia mental para os jogadores se situarem melhor na partida.

A unidade básica de medida é o peão. A partir dele, as peças restantes recebem pontuações maiores com base na mobilidade, alcance e impacto que cada peça causa no tabuleiro. A tabela universal de valores funciona desta maneira:

• Peão: 1 ponto
• Cavalo: 3 pontos
• Bispo: 3 pontos
• Torre: 5 pontos
• Dama: 9 pontos
• Rei: Valor inestimável (infinito)

Como mencionado antes, o Rei possui valor infinito porque a captura significa o fim da partida, a situação de troca de peças ou sacrifício para posteriormente continuar a partida basicamente é impossível de ocorrer com o Rei.

O propósito dessa pontuação é auxiliar o jogador nas trocas materiais. Durante o meio-jogo, é muito comum ocorrer troca de peças, a matemática desse sistema ajuda a decidir se a troca vale a pena ou não.

Por exemplo, capturar a Torre inimiga (que vale 5 pontos) em troca do seu cavalo (com 3 pontos) é uma jogada vantajosa, pois o seu ataque foi mais significativo que o do oponente. Porém, caso capture um Bispo (3 pontos) em troca de sua Dama (9 pontos), o jogador estará com uma grande desvantagem.

Todavia, mesmo que o jogador faça trocas matematicamente desvantajosas, movimentos estratégicos podem mudar completamente o cenário da partida. Um jogador pode sacrificar a peça mais valiosa de propósito e conseguir criar um xeque-mate por conta dessa perda.'
WHERE title = 'Valor relativo das peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 2);

-- =============================================
-- Capítulo 3 — Movimento das Peças
-- =============================================

UPDATE subtopics SET content = 'O peão representa a infantaria do xadrez. Embora seja a peça menos valiosa e uma das mais básicas do jogo, ele possui regras de movimento únicas e orquestra o ritmo da partida. A principal característica dessa peça é que, exceto no primeiro movimento do peão, ela só poderá se mover uma casa para frente a cada turno. Além disso, o peão jamais pode voltar para trás, então cada jogada precisa ser bem analisada, já que é um movimento para frente sem volta e altera permanentemente as possibilidades de movimentos no tabuleiro.

A exceção na primeira jogada é proposital e criada para dar mais velocidade ao início do combate. Isso acontece pois, no movimento inicial de cada peão, a peça tem direito a se movimentar uma ou duas casas para frente. Contudo, essa opção só está disponível no primeiro passo da peça em questão. Após o peão sair do lugar, a peça voltará à regra normal, caminhando somente uma casa para frente por turno pelo restante da partida.'
WHERE title = 'Movimento do peão'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET content = 'A Torre é considerada uma peça pesada no jogo e atua como uma fortaleza. Como o formato da peça sugere, ela tem movimentos rígidos e diretos. O deslocamento da Torre ocorre somente por linhas retas, sejam elas colunas (vias verticais) ou fileiras (vias horizontais). Visualmente, o alcance da Torre forma uma grande cruz que percorre o tabuleiro.

A grande vantagem dessa peça é a liberdade de movimento que pode alcançar. Caso não haja outras peças pelo caminho da torre, ela pode se movimentar a qualquer distância desejada em um único turno.

No entanto, essa peça tem a limitação de não poder pular obstáculos. Caso haja alguma peça em sua trajetória, a Torre deverá interromper o próprio movimento. Por conta dessa característica, ela possui mais vantagem nas fases mais avançadas da partida, por haver menos peças no tabuleiro.'
WHERE title = 'Movimento da torre'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET content = 'O Cavalo é uma peça com movimento único e bastante imprevisível. Diferente das outras peças, que atuam em linhas contínuas, o Cavalo se movimenta desenhando visualmente o formato de "L" no tabuleiro. Em outras palavras, a peça primeiro se movimenta reto para duas casas (norte, sul, leste ou oeste) e depois uma casa perpendicular, onde o Cavalo vira 90 graus.

O diferencial do Cavalo é a habilidade de pular sobre outras peças que estiverem em seu caminho, não importando se a trajetória está bloqueada por aliados ou adversários. A única casa que importa para essa peça é a casa final do movimento. Isso torna o Cavalo perigoso nas jogadas táticas e extremamente útil em regiões fechadas, onde há congestionamento de peças.

Um truque visual que pode auxiliar os jogadores a confirmar se movimentaram o cavalo corretamente é observar as cores das casas em que o Cavalo atuou. Essa peça sempre terminará em uma casa de cor oposta da que começou o turno. Em outras palavras, se o Cavalo começou a jogada em uma casa de cor clara, ele irá terminar em uma casa de cor escura, e vice-versa.'
WHERE title = 'Movimento do cavalo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET content = 'Se a Torre é quem usa as linhas retas, o Bispo é especialista nas linhas diagonais. Essa peça se movimenta somente pelas vias diagonais do tabuleiro, formando visualmente um "X" em seu alcance. Assim como a Torre, o Bispo é uma peça de longo alcance e, caso não haja obstáculos pelo caminho, pode percorrer o tabuleiro livremente pelas diagonais.

Entretanto, se houver outras peças pela trajetória, sejam aliadas ou inimigas, o Bispo não pode ultrapassar e deverá interromper o movimento.

Uma característica marcante do Bispo é que, por conta do estilo de movimento que possui, a peça sempre estará na mesma cor durante toda a partida. Isso significa que os Bispos situados em casas escuras sempre estarão nessas casas até o fim do jogo, assim como os Bispos que começam em casas claras continuarão na mesma cor clara. Por conta disso, o jogador possui dois Bispos, pois um ocupa a casa clara e o outro, a casa escura, formando uma dupla poderosa e que complementa o potencial um do outro.'
WHERE title = 'Movimento do bispo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET content = 'A Dama (também conhecida como Rainha) é a peça mais poderosa, letal e com maior liberdade de movimento no xadrez. Atua como artilharia pesada e possui um alcance que domina o tabuleiro.

O movimento da Dama pode ser considerado visualmente como a junção do movimento do Bispo e da Torre, isso significa que ela pode se movimentar tanto por linhas retas (colunas e fileiras) como por linhas diagonais. Além disso, a Dama não possui limite de distância, podendo percorrer quantas casas forem necessárias em seu domínio durante a jogada, desde que não haja obstáculos em seu caminho.

Contudo, assim como a Torre e o Bispo, a maior limitação da Dama é a impossibilidade de pular obstáculos. Por conta disso, caso haja alguma peça, seja aliada ou inimiga, no caminho, a Dama também deverá parar o movimento.'
WHERE title = 'Movimento da dama'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET content = 'O Rei é a peça mais valiosa e mais importante do jogo, como já foi dito anteriormente. Entretanto, mesmo com esse poder, ainda é uma das peças mais limitadas quanto ao movimento. Assim como a Dama, o Rei pode se movimentar tanto em linhas retas (horizontais e verticais) como nas diagonais, mas com o detalhe de que a peça só poderá se mover por uma casa. Isso torna o Rei vulnerável e que precisa ser protegido pelas outras peças durante quase toda a partida.

Além da restrição de distância, existe uma regra muito importante relacionada a essa peça no xadrez, em que o jogador jamais poderá movimentar seu Rei para uma casa onde a peça será atacada ou controlada por uma peça adversária. O Rei nunca pode se colocar em perigo propositalmente.

Como consequência dessa regra, os dois Reis adversários nunca podem ficar próximos um do outro no tabuleiro. Eles devem sempre manter pelo menos uma casa de distância entre si.'
WHERE title = 'Movimento do rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

UPDATE subtopics SET content = 'No xadrez, a mecânica de capturar peças ocorre quando a peça encerra o movimento ocupando o mesmo espaço de uma peça adversária. Na prática, o jogador retira a peça inimiga do jogo e coloca sua peça no lugar.

A regra geral de captura abrange quase todas as peças do xadrez (Rei, Dama, Torre, Bispo e Cavalo), e indica que qualquer peça inimiga que estiver no alcance de alguma das peças citadas poderá ser capturada.

Entretanto, o Peão é o único tipo de peça que foge a essa regra. Somente ele pode percorrer o tabuleiro de uma maneira e atacar de outra. Como foi visto antes, o Peão se movimenta para frente. Normalmente, esse movimento é feito em linha reta, mas o seu ataque é realizado pelas casas diagonais à frente do peão. Ou seja, ele não pode capturar peças que estejam diretamente na sua frente, mas qualquer peça adversária que esteja exatamente em uma casa diagonal à frente do Peão pode ser capturada.'
WHERE title = 'Captura de peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 3);

-- =============================================
-- Capítulo 4 — Regras Fundamentais
-- =============================================

UPDATE subtopics SET content = 'É uma mecânica de alerta no xadrez. Acontece quando o Rei está sob ataque de uma peça adversária, indicando que está em risco. Mas a partida não acaba nessa situação, pois existe salvação para o Rei.

Pelas regras do xadrez, o jogador não pode ignorar esse aviso, sendo obrigado a usar seu próximo turno para retirar o Rei da ameaça. Existem três maneiras de retirar o xeque: a primeira é movimentando o Rei para uma casa segura, livre do alcance das peças inimigas; a segunda é bloqueando o ataque, colocando uma peça aliada na trajetória servindo como defesa; e a terceira é capturando a peça inimiga que está realizando o xeque. Se o jogador conseguir realizar algum desses três movimentos, o jogo segue normalmente.'
WHERE title = 'Xeque'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET content = 'É a evolução letal do xeque e decreta o fim imediato da partida. Também indica que o Rei está ameaçado por uma peça inimiga, mas a grande diferença é que o jogador não tem possibilidade de sair da ameaça.

Em uma situação de xeque-mate, o jogador não consegue movimentar o Rei para alguma casa segura, não há nenhuma peça aliada que bloqueie o ataque inimigo e é impossível capturar a peça que está atacando. Quando essa armadilha é criada e nenhum movimento é capaz de impedir o xeque, o jogo acaba imediatamente, decretando a vitória de quem atacou.'
WHERE title = 'Xeque-mate'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET content = 'O afogamento (também conhecido como "Rei afogado") ocorre quando o jogador não consegue fazer nenhum movimento válido, mas ao mesmo tempo o rei não está em xeque se continuar onde está.

Pelas regras, o jogador é obrigado a mover o Rei ou as outras peças durante seu turno, além de ser proibido mover o Rei para uma casa atacada. Como o jogador precisa jogar, mas não tem para onde ir, ocorre o empate imediato por afogamento. Isso costuma acontecer quando alguém na partida tem muita vantagem, mas prendeu o Rei adversário sem atacá-lo, eliminando a chance de qualquer vitória que poderia ter.'
WHERE title = 'Empate por afogamento'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET content = 'O xadrez é um jogo de progresso, mas é comum os jogadores entrarem em um ciclo exaustivo com muitas jogadas repetidas. Para otimizar a partida, foi criado o empate por repetição, que acontece quando, durante o jogo, a mesma posição do tabuleiro se repete exatamente três vezes.

Um jogador que está em grande desvantagem na partida pode se aproveitar dessa regra para alcançar o empate. Isso ocorre, por exemplo, quando um jogador está prestes a perder a partida e percebe o xeque-mate se aproximando a cada turno. Como recurso de sobrevivência, ele encontra uma brecha para dar xeques infinitos no Rei adversário, obrigando o oponente a repetir a ir para uma casa e voltar para a casa anterior, repetidas vezes. Quando a mesma situação ocorre três vezes, o jogador que está tentando se salvar pode pedir empate ao árbitro, assim escapando da derrota.'
WHERE title = 'Empate por repetição'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET content = 'Para evitar que uma partida se prolongue por muito tempo quando nenhum dos jogadores consegue dar o xeque-mate, criou-se a regra dos 50 lances. A regra diz que um empate pode ser reivindicado se acontecerem exatamente 50 lances completos (50 jogadas das peças brancas e 50 jogadas das peças pretas) sem captura de peças ou movimento de peão.

A lógica por trás dessa regra ocorre pelo impacto que o movimento do peão e da captura têm na partida, pois modificam a estrutura do jogo de forma irreversível. Se 50 lances acontecerem sem que nada disso ocorra, significa que os jogadores estão "passeando" com suas peças pelo tabuleiro sem nenhuma mudança significativa. Sendo assim, qualquer um dos lados pode pedir o empate.'
WHERE title = 'Regra dos 50 lances'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET content = 'É um tipo de empate que acontece quando ambos os jogadores não possuem peças adequadas para um xeque-mate, tornando a vitória impossível para ambos os lados.

Se uma partida chegar a essa situação, o empate é declarado imediatamente. Isso pode acontecer em situações como: ter somente Rei contra Rei (eles nunca podem ficar próximos entre si), um Rei e um Cavalo contra o Rei inimigo solitário, um Rei e um Bispo contra o Rei inimigo solitário, etc. Nessas condições, é geometricamente impossível ocorrer um xeque-mate.'
WHERE title = 'Material insuficiente'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

UPDATE subtopics SET content = 'É comum existir partidas com limites de tempo, com um relógio duplo específico para o jogo. A duração inicial é a mesma para os dois competidores, mas o relógio passa apenas durante o tempo da vez do jogador.

Ao terminar a jogada, o jogador precisa pausar o relógio e automaticamente irá começar a descontar o tempo do adversário. Se o tempo de um jogador acabar, ele perde a partida imediatamente. Porém, pode acontecer de o tempo de um jogador acabar, mas o adversário não ter peças suficientes para realizar um xeque-mate, e isso gera o empate.'
WHERE title = 'Tempo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 4);

-- =============================================
-- Capítulo 5 — Regras Especiais
-- =============================================

UPDATE subtopics SET content = 'O roque pequeno é um movimento especial que envolve o rei e a torre do lado direito do tabuleiro. Durante essa jogada, o rei anda duas casas em direção à torre, enquanto a torre passa por cima dele e fica posicionada logo ao seu lado. Esse movimento é muito importante porque consegue cumprir duas funções ao mesmo tempo: proteger o rei e desenvolver a torre para uma posição mais ativa.

No início da partida, o rei normalmente fica no centro do tabuleiro, uma região muito perigosa, pois é onde costumam acontecer ataques rápidos e trocas de peças. Ao realizar o roque pequeno, o rei fica mais protegido próximo da borda, dificultando ataques diretos do adversário. Além disso, a torre deixa de ficar presa no canto e passa a participar mais do jogo.

Entretanto, existem algumas condições obrigatórias para que o roque pequeno possa ser realizado. O rei e a torre envolvidos não podem ter se movido anteriormente na partida. Também não pode haver nenhuma peça entre eles. Além disso, o rei não pode estar em xeque, nem atravessar casas atacadas por peças adversárias. Com isso, aprende-se que o roque não é apenas um movimento automático, mas uma decisão estratégica que exige atenção à posição.

Aprender o momento certo de realizar o roque pequeno ajudará o jogador a desenvolver partidas mais seguras e organizadas, evitando deixar o rei vulnerável logo nos primeiros movimentos.'
WHERE title = 'Roque pequeno'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET content = 'O roque grande também é um movimento especial envolvendo o rei e uma torre. Nesse caso, é utilizada a torre do lado esquerdo. O rei anda duas casas em direção à torre, e a torre pula para o lado do rei, ficando ao lado dele após o movimento. Apesar de parecer semelhante ao roque pequeno, o resultado final da posição costuma gerar ideias diferentes no jogo.

Geralmente, o roque grande deixa o rei um pouco mais exposto do que o roque pequeno, pois normalmente existem menos peões protegendo essa região do tabuleiro. Em compensação, ele pode criar posições mais agressivas, permitindo que as torres entrem rapidamente no jogo e que ataques sejam iniciados com maior velocidade. Por isso, muitos jogadores utilizam esse roque quando desejam buscar partidas mais ofensivas.

As regras para realizar o roque grande são praticamente as mesmas do roque pequeno. O rei e a torre não podem ter se movido antes, não pode haver peças entre eles e o rei também não pode estar em xeque ou atravessar casas ameaçadas pelo adversário.

Compreender a diferença entre os dois tipos de roque ajuda o jogador a escolher melhor seus planos durante a abertura. Assim, ele começa a perceber que cada decisão no xadrez influencia diretamente a forma como a partida será conduzida.'
WHERE title = 'Roque grande'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET content = 'O en passant é um movimento que costuma causar dúvida em iniciantes, justamente porque funciona de maneira diferente das capturas normais dos peões. Porém, entendendo sua lógica, ele se torna bastante simples.

O en passant acontece apenas entre peões e somente em uma situação específica. Quando um peão avança duas casas em seu primeiro movimento e termina ao lado de um peão adversário, o peão adversário pode capturá-lo como se ele tivesse avançado apenas uma casa.

Isso existe porque o peão normalmente controla as casas diagonais próximas dele. Sem essa regra, um peão poderia escapar desse controle ao avançar duas casas de uma só vez. O en passant foi criado justamente para evitar essa vantagem excessiva.

Entretanto, existe uma condição muito importante: essa captura precisa ser feita imediatamente no lance seguinte. Caso o jogador escolha fazer qualquer outro movimento, o direito de capturar en passant desaparece. Isso torna a jogada bastante estratégica, pois o jogador precisa decidir rapidamente se vale a pena utilizá-la ou não.

Aprender o en passant ajuda o jogador a conhecer melhor o funcionamento dos peões e evita erros durante partidas. Além disso, compreender regras especiais como essa melhora a visão tática e aumenta o domínio sobre situações incomuns do jogo.'
WHERE title = 'En passant'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

UPDATE subtopics SET content = 'A promoção acontece quando um peão consegue atravessar todo o tabuleiro e chega até a última fileira do lado adversário. Nesse momento, ele deixa de ser um peão e pode ser transformado em outra peça. Na maioria das vezes, os jogadores escolhem promover para dama, pois ela é a peça mais forte do jogo e possui grande capacidade ofensiva.

Porém, a promoção não é limitada apenas à dama. Também é possível escolher torre, bispo ou cavalo. Em algumas situações específicas, escolher outra peça pode ser mais vantajoso estrategicamente. Isso ensina que no xadrez nem sempre a escolha aparentemente mais forte será a melhor para aquela posição.

A promoção é extremamente importante nos finais de partida. Muitas vezes, um jogador que parecia em desvantagem consegue vencer apenas porque conseguiu promover um peão. Por isso, proteger peões avançados e impedir peões adversários avançarem é algo fundamental.

Com isso, o jogador começa a entender que cada peão possui um enorme potencial e que mesmo pequenas vantagens podem decidir uma partida inteira.'
WHERE title = 'Promoção do peão'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 5);

-- =============================================
-- Capítulo 6 — Princípios Básicos de Abertura
-- =============================================

UPDATE subtopics SET content = 'O centro do tabuleiro é formado pelas casas mais importantes da partida, pois a partir delas as peças conseguem se mover com maior liberdade. Quando um jogador controla o centro, suas peças normalmente possuem mais espaço para atacar, defender e criar ameaças.

Peões colocados no centro ajudam a abrir caminho para bispos e damas, além de dificultarem o desenvolvimento do adversário. Por isso, muitos jogadores iniciam a partida movendo peões centrais logo nos primeiros lances.

Ter domínio do centro também facilita ataques futuros e melhora a coordenação das peças. Quem controla essa região geralmente consegue impor o ritmo da partida e obrigar o adversário a reagir aos seus planos.

Aprender esse conceito ajudará o jogador a fazer aberturas mais fortes e a compreender melhor por que certas jogadas são consideradas boas no início da partida.'
WHERE title = 'Controle do centro'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET content = 'Desenvolvimento significa tirar as peças de suas posições iniciais e colocá-las em casas úteis e ativas.

Peças paradas no início do jogo possuem pouco impacto na partida. Já peças desenvolvidas conseguem participar de ataques, defender aliados e controlar regiões importantes do tabuleiro. Por isso, desenvolver rapidamente as peças é um dos objetivos principais da abertura.

Normalmente, cavalos e bispos devem ser desenvolvidos cedo, pois ajudam no controle central e facilitam a realização do roque. Depois disso, torres e dama podem entrar mais ativamente no jogo.

Um erro comum entre iniciantes é mover muitos peões ou usar apenas uma peça repetidamente enquanto outras continuam presas. Isso atrasa o desenvolvimento geral e pode deixar o jogador em desvantagem.

Compreendendo esse princípio, o jogador conseguirá criar posições mais organizadas e preparar melhor suas estratégias para o meio-jogo.'
WHERE title = 'Desenvolvimento das peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET content = 'O rei é a peça mais importante do jogo. Se ele estiver exposto, ataques rápidos podem surgir e a partida pode acabar em poucos lances. Por isso, a segurança do rei deve ser tratada como prioridade durante toda a abertura.

Uma das principais maneiras de proteger o rei é através do roque, movimento especial que vimos anteriormente. Essa jogada retira o rei do centro do tabuleiro e coloca a torre em atividade ao mesmo tempo. Além disso, os peões próximos ao rei ajudam a criar uma barreira defensiva importante.

Muitos ataques fortes no xadrez acontecem justamente contra reis que permaneceram no centro por muito tempo. Portanto, realizar o roque cedo costuma ser uma decisão segura e eficiente.

Aprender esse conceito ajudará o jogador a evitar derrotas rápidas e a criar posições mais resistentes contra ataques adversários.'
WHERE title = 'Segurança do rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET content = 'No xadrez, tempo é algo muito valioso. Cada jogada utilizada deve ajudar no desenvolvimento da posição. Por isso, mover a mesma peça várias vezes logo no início da partida normalmente não é uma boa ideia.

Quando um jogador utiliza muitos lances com apenas uma peça, as outras acabam ficando paradas e sem participação no jogo. Enquanto isso, o adversário pode aproveitar para desenvolver várias peças ao mesmo tempo, controlar o centro e iniciar ameaças rapidamente.

Isso não significa que nunca será necessário mover uma mesma peça duas vezes na abertura. Em algumas posições específicas isso pode acontecer. Porém, na maioria das situações, o ideal é primeiro desenvolver o maior número possível de peças antes de começar a repetir movimentos.

Esse princípio também ajuda o jogador a entender a importância de criar um jogo organizado. Em vez de fazer movimentos aleatórios ou perseguições desnecessárias, o jogador aprende a melhorar toda a sua posição aos poucos.

Com isso, será possível entrar no meio-jogo com mais peças ativas, maior controle do tabuleiro e menos riscos de sofrer ataques perigosos.'
WHERE title = 'Não mover a mesma peça várias vezes'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET content = 'Conectar as torres significa deixar o caminho livre entre elas. Isso normalmente acontece depois que cavalos, bispos e dama saem das casas iniciais. Quando as torres conseguem "se enxergar" horizontalmente, dizemos que estão conectadas.

Torres conectadas trabalham muito melhor juntas. Uma pode defender a outra e ambas conseguem controlar colunas abertas com mais eficiência. Além disso, elas passam a participar mais ativamente das estratégias ofensivas e defensivas.

Em muitas partidas, jogadores iniciantes acabam deixando as torres presas por muito tempo nos cantos do tabuleiro. Isso faz com que peças muito fortes fiquem sem utilidade durante vários lances. Por isso, conectar as torres é um sinal de bom desenvolvimento.

Quando todas as peças já estão ativas, o rei está seguro e as torres estão conectadas, normalmente a fase de abertura está chegando ao fim. A partir daí, o jogo começa a entrar no meio-jogo, onde os planos estratégicos ficam ainda mais importantes.'
WHERE title = 'Conectar as torres'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

UPDATE subtopics SET content = 'Existem muitas aberturas conhecidas no xadrez. Essas aberturas foram estudadas durante muitos anos e ajudam os jogadores a começarem a partida de maneira organizada e eficiente.

As aberturas funcionam como modelos de desenvolvimento. Elas mostram ideias estratégicas, posições comuns e planos que costumam funcionar bem. Isso ajuda o jogador a evitar erros frequentes logo nos primeiros movimentos.

Porém, é importante compreender que decorar movimentos não é suficiente. Todas as boas aberturas seguem os mesmos princípios básicos já explicados neste capítulo, como controle do centro, desenvolvimento das peças e segurança do rei.

Quando o jogador entende esses princípios, ele consegue jogar bem mesmo sem conhecer profundamente uma abertura específica. Isso porque passa a compreender a lógica por trás dos movimentos.

Além disso, estudar aberturas ajuda a prever ideias do adversário e melhora a capacidade de planejamento. Com o tempo, o jogador começa a reconhecer padrões e a tomar decisões mais rápidas e seguras durante a partida.'
WHERE title = 'Padronização de aberturas'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 6);

-- =============================================
-- Capítulo 7 — Noções Básicas de Tática
-- =============================================

UPDATE subtopics SET content = 'O ataque duplo acontece quando uma peça ameaça duas peças adversárias ao mesmo tempo. Isso obriga o adversário a escolher qual delas salvar, normalmente perdendo a outra peça.

Os cavalos são peças muito conhecidas por realizar garfos, pois conseguem atacar várias casas simultaneamente de forma difícil de prever. Entretanto, qualquer peça pode realizar um ataque duplo dependendo da posição.

Essa tática é muito poderosa porque cria situações onde o adversário não consegue responder a todas as ameaças ao mesmo tempo. Além disso, ela ajuda o jogador a ganhar material e aumentar sua vantagem na partida.

Aprender a identificar ataques duplos melhora bastante a visão tática e ajuda o jogador tanto a criar ameaças quanto a evitar cair em armadilhas semelhantes.'
WHERE title = 'Ataque duplo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET content = 'A cravada acontece quando uma peça fica impedida de se mover porque está protegendo outra peça mais importante atrás dela. Se a peça cravada sair do lugar, a peça mais valiosa ficará vulnerável à captura.

Existem situações em que a peça atrás é o próprio rei. Nesses casos, a peça cravada praticamente não pode se mover, já que isso colocaria o rei em xeque, algo proibido pelas regras do jogo.

Essa tática é muito útil porque limita os movimentos do adversário e pode gerar pressão constante sobre determinadas peças. Muitas vezes, jogadores conseguem ganhar material apenas explorando uma cravada corretamente.

Além disso, aprender a reconhecer peças cravadas ajuda o jogador a evitar erros defensivos e melhora sua capacidade de cálculo durante a partida.'
WHERE title = 'Cravada'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET content = 'O ataque descoberto é uma tática bastante importante. Esse tipo de jogada acontece quando uma peça se move e revela o ataque de outra peça que estava bloqueada atrás dela.

Imagine, por exemplo, uma torre apontando para uma peça adversária, mas com um cavalo aliado na frente impedindo o ataque. Se o cavalo sair da frente no momento certo, o ataque da torre será revelado automaticamente. Isso é uma descoberta.

Essa tática é poderosa porque o adversário muitas vezes presta atenção apenas na peça que está na frente e acaba esquecendo do ataque escondido atrás dela.

Além disso, a peça que se move pode criar uma ameaça nova ao mesmo tempo em que revela outra ameaça. Isso gera jogadas muito fortes e difíceis de defender.'
WHERE title = 'Descoberta'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET content = 'O ataque descoberto acontece quando o ataque revelado é um xeque contra o rei adversário. Isso torna a jogada extremamente forte, pois o rei obrigatoriamente precisa ser defendido.

Enquanto o adversário é forçado a responder ao xeque, a peça que se moveu inicialmente pode criar outra ameaça ao mesmo tempo. Isso gera situações muito difíceis de calcular e defender.

Muitas vezes, o ataque descoberto permite ganhar peças importantes ou até finalizar a partida rapidamente. Por isso, jogadores experientes utilizam bastante esse recurso em combinações ofensivas.

Aprender essa tática ajuda o jogador a desenvolver ataques mais perigosos e também melhora sua atenção defensiva, evitando deixar alinhamentos perigosos no tabuleiro.'
WHERE title = 'Ataque descoberto'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET content = 'Uma das habilidades mais importantes no xadrez é reconhecer oportunidades de xeque-mate rapidamente. O mate em 1 representa justamente isso: uma posição onde a partida pode ser vencida em apenas um movimento.

Identificar mates em 1 ajuda o jogador a finalizar partidas sem desperdiçar oportunidades. Além disso, também é fundamental aprender a perceber quando o adversário está ameaçando esse tipo de jogada.

Muitas vezes, jogadores iniciantes acabam focando apenas em capturar peças e esquecem de observar ameaças diretas contra o rei. Isso pode fazer com que percam partidas mesmo estando em vantagem material.

Treinar mates simples melhora bastante a visão de jogo e aumenta a capacidade de encontrar ataques decisivos. Aos poucos, o jogador começa a enxergar padrões de xeque-mate com mais facilidade.

Com isso, será possível tanto atacar com mais eficiência quanto defender posições perigosas de maneira mais segura.'
WHERE title = 'Mate em 1'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET content = 'Os peões podem parecer peças simples, mas sua organização influencia profundamente toda a estratégia da partida. A forma como os peões estão distribuídos no tabuleiro é chamada de estrutura de peões.

Uma boa estrutura de peões ajuda na defesa do rei, no controle de espaço e na movimentação das outras peças. Já uma estrutura ruim pode criar fraquezas difíceis de corrigir.

Os peões possuem uma característica muito importante: eles não podem voltar para trás. Isso significa que cada movimento de peão é definitivo e precisa ser pensado com cuidado.

Por isso, mover peões de maneira aleatória costuma gerar problemas. Algumas situações devem ser evitadas, como deixar o rei exposto, criar peões isolados ou acumular vários peões na mesma coluna.

Aprender sobre estrutura de peões ajuda o jogador a construir posições mais sólidas e a entender melhor os planos estratégicos do meio-jogo e do final.'
WHERE title = 'Estrutura de peões'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET content = 'O CCT é um método muito útil para ajudar na tomada de decisões durante a partida. A sigla significa Checks, Captures e Threats, ou seja: xeques, capturas e ameaças.

A ideia desse método é simples. Antes de realizar uma jogada, o jogador deve analisar se existem xeques possíveis, capturas importantes ou ameaças fortes disponíveis na posição.

Isso ajuda a evitar movimentos feitos apenas no impulso ou no achismo. Muitas vezes, uma jogada aparentemente comum esconde uma combinação tática poderosa.

O CCT também melhora a capacidade de cálculo, pois obriga o jogador a analisar lances forçados antes de pensar em planos mais complexos. Como xeques e capturas normalmente exigem respostas imediatas, eles costumam ser os primeiros movimentos que devem ser avaliados.

Com o tempo, utilizar esse método faz o jogador pensar de maneira mais organizada e estratégica, reduzindo erros simples e aumentando a qualidade das decisões durante a partida.'
WHERE title = 'CCT (Checks, Captures, Threats)'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

UPDATE subtopics SET content = 'Os peões podem parecer peças simples, mas sua organização influencia profundamente toda a estratégia da partida. A forma como os peões estão distribuídos no tabuleiro é chamada de estrutura de peões.

Uma boa estrutura de peões ajuda na defesa do rei, no controle de espaço e na movimentação das outras peças. Já uma estrutura ruim pode criar fraquezas difíceis de corrigir.

Os peões possuem uma característica muito importante: eles não podem voltar para trás. Isso significa que cada movimento de peão é definitivo e precisa ser pensado com cuidado.

Por isso, mover peões de maneira aleatória costuma gerar problemas. Algumas situações devem ser evitadas, como deixar o rei exposto, criar peões isolados ou acumular vários peões na mesma coluna.

Aprender sobre estrutura de peões ajuda o jogador a construir posições mais sólidas e a entender melhor os planos estratégicos do meio-jogo e do final.'
WHERE title = 'Estrutura de peões'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 7);

-- =============================================
-- Capítulo 8 — Como desenvolver o meio-jogo
-- =============================================

UPDATE subtopics SET content = 'O meio-jogo é a fase mais dinâmica e complexa de uma partida de xadrez. Depois que a abertura termina e as peças já estão desenvolvidas, o jogo entra em uma etapa onde os planos e estratégias passam a ter ainda mais importância.

O meio-jogo é a fase em que normalmente acontecem os principais ataques, trocas de peças e disputas por espaço no tabuleiro. Diferente da abertura, onde muitos movimentos seguem princípios conhecidos, o meio-jogo possui muito mais possibilidades. Isso faz com que cada posição seja diferente e exija bastante atenção.

Nessa etapa, o jogador precisa analisar constantemente quais peças estão fortes, quais estão vulneráveis e quais regiões do tabuleiro podem ser exploradas. Muitas vezes, uma pequena vantagem de espaço ou posicionamento pode se transformar em um ataque muito perigoso alguns lances depois.

Além disso, o meio-jogo é a fase em que os erros costumam ser mais punidos. Como existem muitas peças ativas ao mesmo tempo, uma jogada mal calculada pode causar perdas de material ou até um xeque-mate rápido.

Por isso, entender o meio-jogo ajuda o jogador a deixar de fazer movimentos aleatórios e começar a construir ideias mais organizadas dentro da partida. Com isso, será possível criar planos mais eficientes e compreender melhor o funcionamento estratégico do xadrez.'
WHERE title = 'O que é o meio-jogo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

UPDATE subtopics SET content = 'No xadrez, jogar apenas respondendo ao adversário sem um objetivo definido normalmente leva a posições ruins. Durante o meio-jogo, cada movimento deve fazer parte de uma ideia maior. Essas ideias são chamadas de planos.

Um plano pode ter vários objetivos diferentes. O jogador pode tentar atacar o rei adversário, dominar uma coluna aberta, melhorar uma peça mal posicionada ou criar um peão passado, por exemplo. Tudo depende da posição existente no tabuleiro.

Criar planos exige observar alguns fatores importantes, como a segurança dos reis, o posicionamento das peças, a estrutura de peões e os espaços controlados por cada jogador. A partir dessas informações, o jogador começa a decidir quais regiões atacar e quais peças melhorar.

Muitos iniciantes cometem o erro de movimentar peças sem um propósito claro. Isso faz com que a posição fique desorganizada e permite que o adversário tome o controle da partida. Quando existe um plano, cada jogada passa a ter um motivo.

Com isso, o jogador aprende a pensar no xadrez de forma mais estratégica, entendendo não apenas qual lance fazer, mas também por que aquele lance é importante para a posição.'
WHERE title = 'Planos no meio-jogo'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

UPDATE subtopics SET content = 'Uma das estratégias mais conhecidas e perigosas do meio-jogo é o ataque ao rei adversário. Como o objetivo final do xadrez é dar xeque-mate, muitos planos durante a partida giram em torno de enfraquecer a proteção do rei inimigo.

Um ataque ao rei geralmente começa quando o adversário possui poucas peças defendendo essa região ou quando existem fraquezas próximas ao roque. Peões avançados de maneira incorreta ou peças mal posicionadas podem facilitar bastante esse tipo de ataque.

Durante um ataque, é comum que o jogador tente abrir linhas para suas peças entrarem na posição inimiga. Torres e damas costumam ser muito perigosas em colunas abertas, enquanto bispos e cavalos ajudam criando ameaças próximas ao rei.

Em algumas situações, jogadores sacrificam material para abrir espaço e aumentar a pressão ofensiva. Isso acontece porque um rei vulnerável muitas vezes vale mais do que a vantagem material momentânea.

Entretanto, atacar sem preparação também pode ser perigoso. Um ataque mal planejado pode deixar peças expostas e permitir contra-ataques fortes do adversário. Por isso, antes de iniciar um ataque, é importante garantir que as peças estejam coordenadas e prontas para participar da ofensiva.'
WHERE title = 'Ataque ao rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

UPDATE subtopics SET content = 'Casas fracas são casas que não podem mais ser protegidas por peões. Isso normalmente acontece depois que determinados peões avançam ou são trocados. Como os peões não conseguem voltar para trás, essas fraquezas costumam ser permanentes.

Uma casa fraca pode se tornar um excelente local para posicionar peças fortes, principalmente cavalos. Quando uma peça ocupa uma casa fraca protegida por aliados, ela pode exercer enorme pressão sobre a posição adversária.

Por exemplo, se um cavalo consegue chegar em uma casa central onde não pode ser atacado por peões, ele frequentemente se torna uma peça extremamente poderosa. Nessas situações, o adversário é obrigado a usar peças mais importantes para tentar expulsá-lo ou simplesmente aceitar aquela desvantagem estratégica.

Entender casas fracas ajuda o jogador a perceber que o xadrez não é feito apenas de ataques diretos. Muitas vezes, controlar uma região importante do tabuleiro aos poucos gera vantagens muito grandes.

Com isso, o jogador começa a valorizar mais o posicionamento e aprende a evitar enfraquecer sua própria estrutura sem necessidade.'
WHERE title = 'Casas fracas'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

UPDATE subtopics SET content = 'Uma coluna aberta é uma coluna sem peões de nenhum dos lados. Já uma coluna semi-aberta possui peões de apenas um jogador. Essas colunas funcionam como caminhos livres para as torres e damas atuarem com maior facilidade.

Torres colocadas em colunas abertas conseguem atacar peças adversárias, invadir o território inimigo e pressionar pontos fracos da posição. Por isso, controlar essas colunas é um objetivo estratégico muito importante.

Muitas vezes, jogadores dobram as torres na mesma coluna para aumentar ainda mais a pressão. Quando isso acontece, fica muito difícil para o adversário defender a região atacada.

Outro ponto importante é a sétima fileira. Quando uma torre consegue entrar nessa região do tabuleiro adversário, ela frequentemente ameaça peões, impede movimentações do rei e cria vários problemas defensivos.

Aprender a utilizar colunas abertas ajuda o jogador a melhorar bastante o uso das torres e entender como transformar vantagens posicionais em ataques concretos.'
WHERE title = 'Colunas abertas e semi-abertas'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

UPDATE subtopics SET content = 'Nem sempre o valor de uma peça depende apenas do tipo dela. Em muitas posições, uma peça aparentemente forte pode estar praticamente sem utilidade, enquanto uma peça teoricamente menor pode dominar completamente o jogo. Por isso existe o conceito de peças boas e peças ruins.

Uma peça boa é aquela que possui liberdade de movimento e consegue influenciar regiões importantes do tabuleiro. Já uma peça ruim é limitada, bloqueada ou incapaz de participar ativamente do jogo.

Por exemplo, um bispo preso atrás dos próprios peões costuma ser considerado uma peça ruim. Em contrapartida, um cavalo bem centralizado pode controlar muitas casas importantes e gerar pressão constante.

Muitos jogadores iniciantes focam apenas em capturar material e esquecem de melhorar o posicionamento das peças. Entretanto, ter peças mal colocadas pode ser quase o mesmo que jogar com menos material.

Por isso, durante o meio-jogo, é muito importante observar constantemente quais peças estão participando da partida e quais precisam ser reposicionadas.'
WHERE title = 'Peças boas vs peças ruins'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

UPDATE subtopics SET content = 'No xadrez, peças trabalhando juntas são muito mais fortes do que peças agindo isoladamente. Quando existe coordenação, as peças conseguem se defender mutuamente, criar ameaças combinadas e controlar mais regiões do tabuleiro ao mesmo tempo.

Um ataque normalmente só funciona quando várias peças participam juntas. Uma dama sozinha raramente consegue atacar um rei protegido adequadamente. Porém, quando torres, bispos, cavalos e dama trabalham em conjunto, as ameaças ficam muito mais perigosas.

A coordenação também é importante na defesa. Peças protegendo umas às outras tornam a posição mais sólida e dificultam táticas do adversário.

Muitos erros acontecem porque jogadores deixam peças "soltas", ou seja, sem proteção. Isso permite ataques táticos e perdas de material. Quando as peças estão coordenadas, essas fraquezas diminuem bastante.

Compreender a importância da coordenação ajuda o jogador a enxergar o tabuleiro como um conjunto completo, e não apenas como peças separadas. Assim, será possível construir posições mais fortes, equilibradas e difíceis de serem atacadas.'
WHERE title = 'Coordenação de peças'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 8);

-- =============================================
-- Capítulo 9 — Formas de ganhar o final de um jogo
-- =============================================

UPDATE subtopics SET content = 'O final é a fase da partida onde restam poucas peças no tabuleiro. Depois de várias trocas ocorridas durante o meio-jogo, o jogo entra em uma etapa mais técnica, onde cada movimento possui enorme importância.

Nos finais, pequenos erros costumam ter consequências muito grandes. Como existem menos peças defendendo o rei e controlando espaços, um único movimento errado pode transformar uma posição vencedora em empate ou derrota.

Além disso, o rei passa a ter uma participação muito mais ativa. Diferente da abertura e do meio-jogo, onde ele geralmente permanece protegido, no final o rei se torna uma peça importante para atacar, defender peões e controlar casas estratégicas.

Outro ponto importante é que vantagens pequenas se tornam mais relevantes. Um simples peão a mais pode ser suficiente para vencer a partida se o jogador souber utilizar corretamente seus recursos.

Por isso, estudar finais ajuda o jogador a desenvolver precisão, paciência e capacidade de cálculo. Muitos jogadores conseguem boas posições durante a partida, mas desperdiçam a vitória por não conhecerem conceitos básicos dessa fase do jogo.'
WHERE title = 'O que é o final'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET content = 'Existem diferentes tipos de finais, e cada um deles exige conhecimentos específicos e maneiras diferentes de jogar.

Os finais mais comuns são os finais de peões, finais de torres e finais de peças menores, como bispos e cavalos. Cada tipo possui ideias estratégicas próprias e técnicas específicas para transformar vantagem em vitória.

Os finais de peões costumam exigir bastante cálculo e precisão, já que muitas vezes uma única jogada define quem conseguirá promover primeiro. Já os finais de torres são conhecidos por serem bastante complexos, porque as torres conseguem atacar peões e reis à distância, criando muitas possibilidades de jogo.

Existem também finais envolvendo dama, cavalo ou bispo. Nessas situações, o jogador precisa entender como utilizar corretamente a mobilidade e as características específicas de cada peça.

Conhecer os diferentes tipos de final ajuda o jogador a tomar melhores decisões ainda durante o meio-jogo. Muitas vezes, um jogador realiza trocas de peças justamente porque sabe que determinado tipo de final será favorável para ele.'
WHERE title = 'Tipos de final'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET content = 'A oposição acontece quando os dois reis ficam frente a frente, separados por apenas uma casa. Nessa situação, o jogador que não possui a vez de jogar geralmente fica em desvantagem, porque será obrigado a ceder espaço ao adversário.

Como um rei não pode ocupar casas controladas pelo outro rei, essa técnica permite limitar bastante os movimentos do adversário. Dessa forma, o jogador consegue abrir caminho para avançar seus peões ou impedir o avanço dos peões inimigos.

Nos finais, controlar espaço com o rei é extremamente importante. Muitas vezes, a vitória depende apenas de conseguir colocar o rei em uma posição mais ativa do que o rei adversário.

Jogadores iniciantes normalmente focam apenas nos peões e esquecem da importância do rei nessa etapa da partida. Entretanto, nos finais, o rei funciona quase como uma peça ofensiva, ajudando diretamente nas estratégias.'
WHERE title = 'Oposição de reis'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET content = 'O objetivo desse final é simples: utilizar o rei para apoiar o avanço do peão até a promoção. Entretanto, mesmo sendo aparentemente fácil, muitas posições exigem bastante precisão.

O rei deve trabalhar junto com o peão, abrindo caminho e impedindo que o rei adversário se aproxime. Em muitos casos, utilizar corretamente a oposição é justamente o que permite ao peão avançar.

Uma ideia muito importante nesse final é controlar as casas à frente do peão. Quando o rei consegue ocupar posições estratégicas, o adversário perde espaço e acaba sendo obrigado a recuar.

Por outro lado, se o rei atacante estiver mal posicionado, o adversário pode conseguir empatar bloqueando o avanço do peão. Isso mostra como até finais simples exigem técnica e atenção.

Aprender esse final ajuda o jogador a compreender vários conceitos importantes ao mesmo tempo, como oposição, atividade do rei e promoção de peões.'
WHERE title = 'Rei e peão vs rei'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET content = 'A regra do quadrado do peão ajuda o jogador a calcular rapidamente se um rei conseguirá alcançar um peão antes da promoção.

A lógica é bastante simples. O jogador imagina um quadrado começando na casa do peão e indo até a casa de promoção. A largura do quadrado deve ter o mesmo tamanho da distância restante até a promoção.

Se o rei adversário estiver dentro desse quadrado, normalmente ele conseguirá alcançar o peão. Caso esteja fora, o peão provavelmente conseguirá promover sem precisar de ajuda.

Essa técnica é muito importante porque permite analisar posições rapidamente sem precisar calcular muitos movimentos detalhadamente. Em partidas com pouco tempo no relógio, isso se torna ainda mais útil.

Além disso, a regra do quadrado ajuda o jogador a entender melhor a velocidade dos peões e a importância da atividade do rei nos finais.'
WHERE title = 'Regra do quadrado do peão'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);

UPDATE subtopics SET content = 'É essencial aprender alguns padrões básicos de xeque-mate. Esses padrões aparecem com frequência e ajudam o jogador a finalizar partidas de maneira segura e eficiente.

Muitos jogadores conseguem vantagem material durante a partida, mas não sabem como transformar essa vantagem em xeque-mate. Por isso, estudar padrões básicos é fundamental para desenvolver confiança nos finais.

Os mates mais conhecidos envolvem dama e rei, torre e rei ou duas peças pesadas trabalhando juntas. Essas combinações ensinam coordenação de peças, controle de espaço e posicionamento correto do rei adversário.

Mate da escadinha: É realizado com duas torres ou com torre e dama. A ideia é empurrar o rei adversário gradualmente até a borda do tabuleiro. Uma peça controla uma fileira enquanto a outra dá xeque, obrigando o rei a recuar. As peças parecem subir ou descer pelo tabuleiro em etapas organizadas, limitando cada vez mais o espaço do rei adversário.

Mate com rei e dama: A dama é utilizada para reduzir o espaço do rei adversário enquanto o próprio rei aliado se aproxima para ajudar no bloqueio das casas de fuga. O processo acontece aos poucos: primeiro a dama limita o espaço do rei adversário, depois o rei aliado avança até ajudar diretamente no encurralamento. Quando o rei inimigo fica preso na borda do tabuleiro, o xeque-mate finalmente acontece.'
WHERE title = 'Padrões básicos de mate'
  AND chapter_id = (SELECT id FROM chapters WHERE display_order = 9);