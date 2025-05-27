import React, { useState, useEffect } from 'react'
import axios from 'axios';
import { PieChart, Pie, Tooltip, ResponsiveContainer, Cell } from 'recharts';
import { useAuth } from './context/AuthContext';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8', '#82CA9D'];

export const ExpensePieChart = () => {
    const [expensePieChartData, setExpensePieChartData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { getUser } = useAuth();

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

    const handleExpensePieChartData = async () => {
        const user = getUser();
        if (!user || !user.username || !user.password) {
            throw new Error('User not authenticated or missing credentials');
        }
        try {
            setLoading(true);
            const response = await fetch('http://localhost:8080/transactions/type?type=EXPENSE', {
                headers: {
                    'Authorization': 'Basic ' + btoa(`${user.username}:${user.password}`),
                    'Content-Type': 'application/json'
                }
            });
            const data = await response.json();
            const processedData = processChartData(data);
            console.log('Processed chart data:', processedData);
            setExpensePieChartData(processedData);
        } catch (error) {
            setError('Error fetching expense pie chart data: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    // Initial load
    useEffect(() => {
        handleExpensePieChartData();
    }, []);

    useEffect(() => {
        const handleTransactionUpdated = () => {
            handleExpensePieChartData();
        };
        window.addEventListener('transactionUpdated', handleTransactionUpdated);
        return () => window.removeEventListener('transactionUpdated', handleTransactionUpdated);
    }, []);

    if (loading) {
        return <div className="flex justify-center items-center h-[400px]">Loading...</div>;
    }

    if (error) {
        return <div className="text-red-500 text-center h-[400px]">{error}</div>;
    }

    return (
        <div className="">
            <div className="h-[400px]">
                <h3 className="text-center mb-4">Expense Categories</h3>
                <ResponsiveContainer width="100%" height="100%">
                    <PieChart>
                        <Pie
                            data={expensePieChartData}
                            dataKey="amount"
                            nameKey="category"
                            cx="50%"
                            cy="50%"
                            outerRadius={150}
                            label={({ name, percent }) => `${name} (${(percent * 100).toFixed(0)}%)`}
                        >
                            {expensePieChartData.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                            ))}
                        </Pie>
                        <Tooltip 
                            formatter={(value) => [`$${value.toFixed(2)}`, 'Amount']}
                        />
                    </PieChart>
                </ResponsiveContainer>
            </div>
        </div>
    )
}
