import React from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const getComplexityValue = (complexity) => {
  if (!complexity) return 1;

  if (complexity.includes("n^")) {
    return parseInt(complexity.split("^")[1]) * 10;
  }
  if (complexity.includes("n")) {
    return 5;
  }
  return 1;
};

const ComplexityChart = ({ timeComplexity, spaceComplexity }) => {
  const data = [
    {
      name: "Time",
      value: getComplexityValue(timeComplexity),
    },
    {
      name: "Space",
      value: getComplexityValue(spaceComplexity),
    },
  ];

  return (
    <div style={{ width: "100%", height: 250 }}>
      <h3>Complexity Visualization</h3>
      <ResponsiveContainer>
        <BarChart data={data}>
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="value" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default ComplexityChart;