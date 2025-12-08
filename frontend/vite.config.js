import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  
  // *** TÄMÄ UUSI 'server' OSA ON KRIITTINEN! ***
  server: {
    proxy: {
      // Kun Front-end yrittää kutsua osoitetta /api/...,
      // Vite ohjaa sen automaattisesti osoitteeseen http://localhost:8080/api/...
      '/api': {
        target: 'http://localhost:8080', 
        changeOrigin: true,
        secure: false, // Tarpeeton HTTPS:lle kehitysympäristössä
      },
    },
  }
  // **********************************************
})