import React from "react";
import "./DryRun.css";

const DryRun = ({ steps }) => {
  if (!steps || steps.length === 0) {
    return (
      <div className="dryrun-container">
        <h2> Dry Run</h2>
        <p className="empty">No execution steps available</p>
      </div>
    );
  }

  return (
    <div className="dryrun-container">
      <h2>Dry Run (Execution Trace)</h2>

      <div className="steps-list">
        {steps.map((step) => (
          <div key={step.step} className="step-card">
            <div className="step-number">Step {step.step}</div>
            <div className="step-desc">{step.description}</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default DryRun;