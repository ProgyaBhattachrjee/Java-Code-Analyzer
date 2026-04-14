import React from 'react';
import './MetricsDisplay.css';
import ComplexityChart from "./ComplexityChart";
const MetricsDisplay = ({ metrics }) => {
  const {
    lineCount,
    wordCount,
    charCount,
    loopCount,
    methodCount,
    ifCount,
    timeComplexity,
    spaceComplexity,
    codeSmells
  } = metrics;

  const complexityColors = {
    'O(1)': 'excellent',
    'O(log n)': 'good',
    'O(n)': 'fair',
    'O(n log n)': 'moderate',
    'O(n^2)': 'poor',
    'O(2^n)': 'very-poor'
  };

  return (
    <div className="metrics-container">
      <h2>Analysis Results</h2>
      
      <div className="metrics-grid">
        <div className="metric-card">
          <h3>Basic Metrics</h3>
          <div className="metric-item">
            <span className="metric-label">Lines:</span>
            <span className="metric-value">{lineCount}</span>
          </div>
          <div className="metric-item">
            <span className="metric-label">Words:</span>
            <span className="metric-value">{wordCount}</span>
          </div>
          <div className="metric-item">
            <span className="metric-label">Characters:</span>
            <span className="metric-value">{charCount}</span>
          </div>
        </div>

        <div className="metric-card">
          <h3>Code Structure</h3>
          <div className="metric-item">
            <span className="metric-label">Loops:</span>
            <span className="metric-value">{loopCount}</span>
          </div>
          <div className="metric-item">
            <span className="metric-label">Methods:</span>
            <span className="metric-value">{methodCount}</span>
          </div>
          <div className="metric-item">
            <span className="metric-label">If Statements:</span>
            <span className="metric-value">{ifCount}</span>
          </div>
        </div>

        <div className="metric-card">
          <h3>Complexity Analysis</h3>
          <div className="metric-item">
            <span className="metric-label">Time Complexity:</span>
            <span className={`metric-value complexity ${complexityColors[timeComplexity] || 'unknown'}`}>
              {timeComplexity}
            </span>
          </div>
          <div className="metric-item">
            <span className="metric-label">Space Complexity:</span>
            <span className={`metric-value complexity ${complexityColors[spaceComplexity] || 'unknown'}`}>
              {spaceComplexity}
            </span>
          </div>
        </div>
      </div>
     <ComplexityChart
  timeComplexity={metrics.timeComplexity}
  spaceComplexity={metrics.spaceComplexity}
/>
      {codeSmells && codeSmells.length > 0 && (
        <div className="code-smells-section">
          <h3>Code Smells Detected</h3>
          <ul className="code-smells-list">
            {codeSmells.map((smell, index) => (
              <li key={index} className="code-smell-item">
                <span className="warning-icon">⚠️</span>
                {smell}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default MetricsDisplay;