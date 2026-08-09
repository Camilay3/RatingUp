const { createCjsPreset } = require('jest-preset-angular/presets');

module.exports = {
	...createCjsPreset(),
	setupFilesAfterEnv: ['<rootDir>/setup-jest.ts'],
	testMatch: ['<rootDir>/src/**/*.spec.ts'],
	collectCoverageFrom: [
		'src/**/*.ts',
		'src/**/*.html',
		'!src/**/*.spec.ts',
		'!src/**/*.d.ts',
		'!src/index.html',
	],
	coverageThreshold: {
		global: {
			branches: 90,
			functions: 90,
			lines: 90,
			statements: 90,
		},
	},
	coverageReporters: ['text', 'text-summary', 'lcov'],
};
