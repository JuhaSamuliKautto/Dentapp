import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { defineCustomElements } from '@uh-design-system/component-library/loader';
defineCustomElements(window);
import '@uh-design-system/component-library/dist/component-library/component-library.css';



createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)