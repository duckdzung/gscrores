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
        rollupOptions: {
            output: {
                manualChunks(id) {
                    if (id.includes('node_modules')) {
                        const basic = id.split('node_modules/')[1];
                        const parts = basic.split('/');

                        // Xử lý package bình thường
                        if (parts[0] !== '.pnpm') {
                            return parts[0];
                        }

                        // Xử lý package từ .pnpm (ví dụ: @babel/core)
                        const packageName = parts[1];
                        return packageName.startsWith('@') ? packageName.split('/')[0] + '/' + parts[2] : packageName;
                    }
                },
            },
        },
    },
});
