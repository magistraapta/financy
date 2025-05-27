import React, { useState, useEffect } from 'react'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { useAuth } from './context/AuthContext';

export const Expense = () => {
    const [expense, setExpense] = useState();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [interval, setInterval] = useState('1m')
    const [filteredExpense, setFilteredExpense] = useState();
    const { getUser } = useAuth();

    const handleExpense = async () => {
        try {
            setLoading(true)
            const user = getUser();
            if (!user || !user.username || !user.password) {
                throw new Error('User not authenticated or missing credentials');
            }

            const response = await fetch('http://localhost:8080/transactions/type?type=EXPENSE', {
                headers: {
                    'Authorization': 'Basic ' + btoa(`${user.username}:${user.password}`),
                    'Content-Type': 'application/json'
                }
            })

            const data = await response.json()
            // Sort the data by date in ascending order
            const sortedData = data.sort((a, b) => new Date(a.date) - new Date(b.date));
            setExpense(sortedData)
        } catch (error) {
            setError(error)
        } finally {
            setLoading(false)
        }
    }

    const filterDataByInterval = (data, selectedInterval) => {
        if (!data) {
            console.log('No data available for filtering');
            return;
        }
        
        const now = new Date();
        let startDate = new Date();

        switch (selectedInterval) {
            case '1m':
                startDate.setMonth(now.getMonth() - 1);
                break;
            case '3m':
                startDate.setMonth(now.getMonth() - 3);
                break;
            case '1y':
                startDate.setFullYear(now.getFullYear() - 1);
                break;
            default:
                startDate.setMonth(now.getMonth() - 1);
        }
        
        const filteredData = data.filter(item => new Date(item.date) >= startDate);
        console.log('Filtered expense data:', filteredData);
        setFilteredExpense(filteredData);
    }

    useEffect(() => {
        if (expense) {
            filterDataByInterval(expense, interval);
        }
    }, [interval, expense]);

    useEffect(() => {
       const handleTransactionAdded = () => {
        handleExpense()
       }

       window.addEventListener('transactionAdded', handleTransactionAdded)
       handleExpense()
       return () => window.removeEventListener('transactionAdded', handleTransactionAdded)
    }, [])

    if (loading) return <div>Loading...</div>
    if (error) return <div>Error: {error.message}</div>

  return (
    <div>
        <h1 className='text-2xl font-bold mb-3'>Expense</h1>
        <div className="flex gap-2 mb-4">
            <button 
                onClick={() => setInterval('1m')}
                className={`px-4 py-2 rounded ${interval === '1m' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
            >
                1 Month
            </button>
            <button 
                onClick={() => setInterval('3m')}
                className={`px-4 py-2 rounded ${interval === '3m' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
            >
                3 Months
            </button>
            <button 
                onClick={() => setInterval('1y')}
                className={`px-4 py-2 rounded ${interval === '1y' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
            >
                1 Year
            </button>
        </div>
        <div className="h-[300px]">
            <ResponsiveContainer width="100%" height="100%">
            <LineChart data={filteredExpense} margin={{ top: 5, right: 20, bottom: 5, left: 0 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis 
                    dataKey="date" 
                    tickFormatter={(date) => new Date(date).toLocaleDateString('en-US', {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit'
                    })}
                />
                <YAxis dataKey="amount"/>
                <Tooltip 
                    labelFormatter={(date) => new Date(date).toLocaleDateString('en-US', {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit'
                    })}
                />
                <Line type="monotone" dataKey="amount" stroke="#d30000" />
            </LineChart>
            </ResponsiveContainer>
        </div>
    </div>
  )
}
