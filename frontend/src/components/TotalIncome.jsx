import React, { useState, useEffect } from 'react'
import { useAuth } from './context/AuthContext';
import axios from 'axios';

export const TotalIncome = () => {
    const [totalIncome, setTotalIncome] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const { getUser } = useAuth();


    const handleTotalIncome = async () => {
        try {
            setLoading(true);
            const user = getUser();
            if (!user || !user.username || !user.password) {
                throw new Error('User not authenticated or missing credentials');
            }
            const response = await axios.get('http://localhost:8080/transactions/total-income', {
                headers: {
                    'Authorization': 'Basic ' + btoa(`${user.username}:${user.password}`),
                    'Content-Type': 'application/json'
                }
            });
            setTotalIncome(response.data);
            console.log(response.data);
        } catch (error) {
            setError('Error fetching total income: ' + error.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        handleTotalIncome();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

  return (
    <div className='p-6 border border-gray-300 rounded-lg bg-white'>
        <p className='text-2xl font-bold'>Total Income: {totalIncome}</p>
    </div>
  )
}
