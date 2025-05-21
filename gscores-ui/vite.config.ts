import path from 'path';
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    resolve: {
        alias: {
            '@': path.resolve(__dirname, './src'),
            '@tabler/icons-react': '@tabler/icons-react/dist/esm/icons/index.mjs',
        },
    },
    build: {
        sourcemap: false,
        rollupOptions: {
            output: {
                manualChunks(id) {
                    if (id.includes('node_modules')) {
                        const basic = id.split('node_modules/')[1];
                        const parts = basic.split('/');

                        if (parts[0] !== '.pnpm') {
                            return parts[0];
                        }

                        const packageName = parts[1];
                        return packageName.startsWith('@') ? packageName.split('/')[0] + '/' + parts[2] : packageName;
                    }
                },
            },
        },
    },
});
