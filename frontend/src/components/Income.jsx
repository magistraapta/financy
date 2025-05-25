import React, { useState, useEffect } from 'react'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { useAuth } from './context/AuthContext';

export const Income = () => {
  const [income, setIncome] = useState();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const { getUser } = useAuth();

  const handleIncome = async () => {
    try {
      setLoading(true)
      const user = getUser();
      if (!user || !user.username || !user.password) {
        throw new Error('User not authenticated or missing credentials');
      }
      
      const response = await fetch('http://localhost:8080/transactions/type?type=INCOME', {
        headers: {
          'Authorization': 'Basic ' + btoa(`${user.username}:${user.password}`),
          'Content-Type': 'application/json'
        }
      })
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data = await response.json()
      setIncome(data)
    } catch (error) {
      setError(error.message)
    } finally {
      setLoading(false)
    }
  }

  // Listen for custom event when a new transaction is added
  useEffect(() => {
    const handleTransactionAdded = () => {
      handleIncome();
    };

    window.addEventListener('transactionAdded', handleTransactionAdded);
    
    // Initial data fetch
    handleIncome();

    return () => {
      window.removeEventListener('transactionAdded', handleTransactionAdded);
    };
  }, []);

  if (loading) return <div>Loading...</div>
  if (error) return <div>Error: {error}</div>

  return (
    <div>
      <h1 className='text-2xl font-bold mb-3'>Income</h1>
      <div className="h-[300px]">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={income} margin={{ top: 5, right: 20, bottom: 5, left: 0 }}>
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
              <Line type="monotone" dataKey="amount" stroke="#82ca9d" />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  )
}
