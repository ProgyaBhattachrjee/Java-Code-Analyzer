import React, { useState } from 'react';
import axios from 'axios';
import './App.css';
import MetricsDisplay from './components/MetricsDisplay';
import FlowChart from './components/FlowChart';
import DryRun from './components/DryRun';
import Editor from "@monaco-editor/react";
function App() {
  const [code, setCode] = useState('');
  const [analysisResult, setAnalysisResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);


const handleAnalyzeCode = async () => {
  if (!code.trim()) {
    setError("Please enter some code before analyzing.");
    return;
  }

  setLoading(true);
  setError(null);

  try {
    const response = await axios.post('http://localhost:8080/api', {
      code: code
    }, {
      headers: {
        'Content-Type': 'application/json',
      },
      timeout: 30000
    });

    console.log('Analysis result:', response.data);
    setAnalysisResult(response.data);

  } catch (err) {
    console.error('Error details:', err);

    if (err.code === 'ECONNABORTED') {
      setError('Request timeout. Please try again.');
    } else if (err.response) {
      setError(`Server error: ${err.response.status} - ${err.response.data}`);
    } else if (err.request) {
      setError('Cannot connect to API. Make sure backend is running.');
    } else {
      setError(`Error: ${err.message}`);
    }

  } finally {
    setLoading(false);
  }
};
  return (
    <div className="App">
      <header className="App-header">
      <h1 className="main-title">Java Code Analyzer</h1>
      </header>

      <main className="App-main">
        <div className="input-section">
          <div className="input-header">
            <label htmlFor="code-input">Enter your code:</label>
          </div>
          <div className="editor-container">
            <Editor
  height="300px"
  defaultLanguage="java"
  value={code}
  onChange={(value) => setCode(value || "")}
  theme="vs-dark"
  options={{
    fontSize: 14,
    minimap: { enabled: false },
    wordWrap: "on",
    automaticLayout: true,
  }}
/>
          </div>
          
          <button 
            className="analyze-btn"
            onClick={handleAnalyzeCode}
            disabled={loading}
          >
            {loading ? (
              <>
                <span className="spinner"></span>
                Analyzing...
              </>
            ) : (
              'Analyze Code'
            )}
          </button>
        </div>

        {error && (
          <div className="error-message">
            <span className="error-icon">⚠️</span>
            {error}
          </div>
        )}

        {analysisResult && (
          <div className="results-section">
            <MetricsDisplay metrics={analysisResult} />
            
            {analysisResult.nodes && analysisResult.edges && (
              <div className="flowchart-section">
                <h2> Code Flow Visualization</h2>
                <FlowChart 
                  nodes={analysisResult.nodes} 
                  edges={analysisResult.edges} 
                />
                <div className="flowchart-legend">
                  <div className="legend-item">
                    <span className="legend-color" style={{background: '#4CAF50'}}></span>
                    <span>Start</span>
                  </div>
                  <div className="legend-item">
                    <span className="legend-color" style={{background: '#f44336'}}></span>
                    <span>End</span>
                  </div>
                  <div className="legend-item">
                    <span className="legend-color" style={{background: '#FF9800'}}></span>
                    <span>Decision/Loop</span>
                  </div>
                  <div className="legend-item">
                    <span className="legend-color" style={{background: '#2196F3'}}></span>
                    <span>Process</span>
                  </div>
                  <div className="legend-item">
                    <span className="legend-line" style={{background: '#FF9800', height: '3px'}}></span>
                    <span>Loop Edge</span>
                  </div>
                </div>
              </div>
            )}
          </div>
        )}
        <DryRun steps={analysisResult?.executionSteps} />
      </main>
    </div>
  );
}

export default App;