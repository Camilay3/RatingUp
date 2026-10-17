export function maskPhoneInput(value: string): { digits: string; masked: string } {
	const digits = value.replace(/\D/g, '').slice(0, 11);
	if (digits.length <= 2) return { digits, masked: digits ? `(${digits}` : '' };
	if (digits.length <= 6) return { digits, masked: digits.replace(/(\d{2})(\d{0,4})/, '($1) $2') };
	if (digits.length <= 10) return { digits, masked: digits.replace(/(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3') };
	return { digits, masked: digits.replace(/(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3') };
}
