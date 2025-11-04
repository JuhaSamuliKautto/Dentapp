import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <ds-button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </ds-button>
        <div>
          <ds-radio-button-group ds-label="Mitä ihmettä" ds-assistive-text="" ds-direction="vertical" ds-value="" ds-error-text="">
            <ds-radio-button ds-text="Value text 1"></ds-radio-button>
            <ds-radio-button ds-text="Value text 2"></ds-radio-button>
            <ds-radio-button ds-text="Value text 3"></ds-radio-button>
            <ds-radio-button ds-text="Value text 4"></ds-radio-button>
         </ds-radio-button-group>
          </div>
          <div>
            <ds-spinner></ds-spinner>
          </div>
          <div>
                <ds-select ds-label="Valitse jtn." ds-placeholder="Select an option" ds-variant="default" ds-clearable="" ds-error-text="" ds-icon="language" ds-value="">
                <ds-option ds-value="option1">Option 1</ds-option><ds-option ds-value="option2">Option 2</ds-option><ds-option ds-value="option3">Option 3</ds-option>
                </ds-select>
          </div>
        <p>
          Edit <code>src/App.jsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </>
  )
}

export default App
