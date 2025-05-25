import React from 'react';
import { PieChart, Pie, Tooltip, ResponsiveContainer, Cell } from 'recharts';
import { pieChartData } from '../dummyData';

export const TransactionType = () => {
  const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-3">Income and Expense Type</h1>
      <div className='grid grid-cols-2 gap-4'>
        <div className="h-[400px]">
            <h3 className='text-center'>Income Type</h3>
            <ResponsiveContainer width="100%" height="100%">
            <PieChart>
                <Pie
                data={pieChartData}
                dataKey="value"
                nameKey="name"
                cx="50%"
                cy="50%"
                outerRadius={150}
                label
                >
                {pieChartData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
                </Pie>
                <Tooltip />
            </PieChart>
            </ResponsiveContainer>
        </div>
        <div className="h-[400px]">
            <h3 className='text-center'>Expense Type</h3>
            <ResponsiveContainer width="100%" height="100%">
            <PieChart>
                <Pie
                data={pieChartData}
                dataKey="value"
                nameKey="name"
                cx="50%"
                cy="50%"
                outerRadius={150}
                label
                >
                {pieChartData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
                </Pie>
                <Tooltip />
            </PieChart>
            </ResponsiveContainer>
        </div>
      </div>
    </div>
  )
}
