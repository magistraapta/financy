import React from 'react'
import { useState, useEffect } from 'react';
import { useAuth } from './context/AuthContext';
import { PieChart, Pie, Tooltip, ResponsiveContainer, Cell } from 'recharts';
const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8', '#82CA9D'];


export const IncomePieChart = () => {
    const [incomePieChartData, setIncomePieChartData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { getUser } = useAuth()

    const processChartData = (transactions) => {
        // Group transactions by category and sum their amounts
        const groupedData = transactions.reduce((acc, transaction) => {
            const category = transaction.category || 'Uncategorized';
            if (!acc[category]) {
                acc[category] = 0;
            }
            acc[category] += transaction.amount;
            return acc;
        }, {});

        // Convert to array format required by recharts
        return Object.entries(groupedData).map(([category, amount]) => ({
            category,
            amount: Math.abs(amount) // Ensure positive values for expenses
        }));
    };

    const handleIncomePieChartData = async () => {
        const user = getUser();
        if (!user || !user.username || !user.password) {
            throw new Error('User not authenticated or missing credentials');
        }
        try {
            const response = await fetch('http://localhost:8080/transactions/type?type=INCOME', {
                headers: {
                    'Authorization': 'Basic ' + btoa(`${user.username}:${user.password}`),
                    'Content-Type': 'application/json'
                }
            });
            const data = await response.json()
            const processedData = processChartData(data);
            setIncomePieChartData(processedData);
        } catch (error) {
            setError('Error fetching income pie chart data: ' + error.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        handleIncomePieChartData();
    }, []);

    if (loading) {
        return <div className="flex justify-center items-center h-[400px]">Loading...</div>;
    }

    if (error) { 
        return <div className="text-red-500 text-center h-[400px]">{error}</div>;
    }

  return (
    <div className="h-[400px]">
        <h3 className='text-center'>Income Categories</h3>
        <ResponsiveContainer width="100%" height="100%">
            <PieChart>
                <Pie
                    data={incomePieChartData}
                    dataKey="amount"
                    nameKey="category"
                    cx="50%"
                    cy="50%"
                    outerRadius={150}
                    label={({ name, percent }) => `${name} (${(percent * 100).toFixed(0)}%)`}
                >
                    {incomePieChartData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                </Pie>
                <Tooltip 
                    formatter={(value) => [`$${value.toFixed(2)}`, 'Amount', 'Category']}
                />
            </PieChart>
        </ResponsiveContainer>
    </div>
  )
}
