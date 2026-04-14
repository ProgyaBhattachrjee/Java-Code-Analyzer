import React from 'react';
import ReactFlow, {
  Background,
  Controls,
  MiniMap,
  Handle,
  Position
} from 'reactflow';
import dagre from 'dagre';
import 'reactflow/dist/style.css';
import './FlowChart.css';

// AUTO LAYOUT FUNCTION
const getLayoutedElements = (nodes, edges) => {
  const dagreGraph = new dagre.graphlib.Graph();
  dagreGraph.setDefaultEdgeLabel(() => ({}));

  // Top → Bottom layout
  dagreGraph.setGraph({ rankdir: 'TB', nodesep: 80, ranksep: 100 });

  // Register nodes
  nodes.forEach((node) => {
    dagreGraph.setNode(node.id.toString(), {
      width: 160,
      height: 60,
    });
  });

  // Register edges
  edges.forEach((edge) => {
    dagreGraph.setEdge(edge.from.toString(), edge.to.toString());
  });

  dagre.layout(dagreGraph);

  // Apply positions
  const layoutedNodes = nodes.map((node) => {
    const pos = dagreGraph.node(node.id.toString());

    return {
      id: node.id.toString(),
      type: 'custom',
      data: {
        label: node.label,
        type: node.type,
      },
      position: {
        x: pos.x - 80,
        y: pos.y - 30,
      },
    };
  });

  const layoutedEdges = edges.map((edge, index) => ({
    id: `e-${index}`,
    source: edge.from.toString(),
    target: edge.to.toString(),
    label: edge.condition || '',
    type: 'smoothstep',
    animated: edge.condition === 'loop',
    style: {
      stroke: edge.condition === 'loop' ? '#FF9800' : '#555',
      strokeWidth: 2,
    },
    labelStyle: {
      fontWeight: 'bold',
      fontSize: '12px',
    },
  }));

  return { nodes: layoutedNodes, edges: layoutedEdges };
};

// CUSTOM NODE UI
const CustomNode = ({ data }) => {
  const getColor = () => {
    switch (data.type) {
      case 'start': return '#4CAF50';
      case 'end': return '#f44336';
      case 'decision': return '#FF9800';
      default: return '#2196F3';
    }
  };

  return (
    <div
      style={{
        background: getColor(),
        color: 'white',
        padding: '10px',
        borderRadius: data.type === 'decision' ? '4px' : '8px',
        minWidth: '120px',
        textAlign: 'center',
        border: '2px solid #222',
      }}
    >
      <Handle type="target" position={Position.Top} />
      <div>{data.label}</div>
      <Handle type="source" position={Position.Bottom} />
    </div>
  );
};

const nodeTypes = {
  custom: CustomNode,
};

const FlowChart = ({ nodes, edges }) => {
  if (!nodes || nodes.length === 0) {
    return <p>No flowchart data</p>;
  }

  const { nodes: flowNodes, edges: flowEdges } =
    getLayoutedElements(nodes, edges);

  return (
    <div style={{ height: '500px', border: '1px solid #ccc' }}>
      <ReactFlow
        nodes={flowNodes}
        edges={flowEdges}
        nodeTypes={nodeTypes}
        fitView
      >
        <Background />
        <Controls />
        <MiniMap />
      </ReactFlow>
    </div>
  );
};

export default FlowChart;