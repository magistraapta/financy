import React, { useState, useEffect } from 'react'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { useAuth } from './context/AuthContext';

export const Expense = () => {
    const [expense, setExpense] = useState();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
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
            setExpense(data)
        } catch (error) {
            setError(error)
        } finally {
            setLoading(false)
        }
    }

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
        <div className="h-[300px]">
            <ResponsiveContainer width="100%" height="100%">
            <LineChart data={expense} margin={{ top: 5, right: 20, bottom: 5, left: 0 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis 
                    dataKey="createdAt" 
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
