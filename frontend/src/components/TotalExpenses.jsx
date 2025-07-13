import React, { useState, useEffect } from 'react'
import { useAuth } from './context/AuthContext';
import axios from 'axios';

export const TotalExpenses = () => {
    const [totalExpenses, setTotalExpenses] = useState(0);
    const [currentMonthExpenses, setCurrentMonthExpenses] = useState(0);
    const [previousMonthExpenses, setPreviousMonthExpenses] = useState(0);
    const [expenseComparison, setExpenseComparison] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const { getUser } = useAuth();

    const handleTotalExpenses = async () => {
        try {
            setLoading(true);
            const user = getUser();
            if (!user || !user.username || !user.password) {
                throw new Error('User not authenticated or missing credentials');
            }
            const response = await axios.get('http://localhost:8080/transactions/total-expenses', {
                headers: {
                    'Authorization': 'Basic ' + btoa(`${user.username}:${user.password}`),
                    'Content-Type': 'application/json'
                }
            });
            setTotalExpenses(response.data);
        } catch (error) {
            setError('Error fetching total expenses: ' + error.message);
        } finally {
            setLoading(false);
        }
    }

    const handleExpenseComparison = async () => {
        try {
            const user = getUser();
            if (!user || !user.username || !user.password) {
                throw new Error('User not authenticated or missing credentials');
            }
            const response = await axios.get('http://localhost:8080/transactions/expense-comparison', {
                headers: {
                    'Authorization': 'Basic ' + btoa(`${user.username}:${user.password}`),
                    'Content-Type': 'application/json'
                }
            });
            setExpenseComparison(response.data);
            setCurrentMonthExpenses(response.data.currentMonthExpenses);
            setPreviousMonthExpenses(response.data.previousMonthExpenses);
        } catch (error) {
            console.error('Error fetching expense comparison:', error);
        }
    }

    useEffect(() => {
        handleTotalExpenses();
        handleExpenseComparison();
    }, []);

    useEffect(() => {
        const handleTransactionAdded = () => {
            handleTotalExpenses();
            handleExpenseComparison();
        };
        window.addEventListener('transactionAdded', handleTransactionAdded);
        return () => window.removeEventListener('transactionAdded', handleTransactionAdded);
    }, []);

    if (loading) { 
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    const getTrendIcon = (trend) => {
        switch (trend) {
            case 'increase':
                return '📈';
            case 'decrease':
                return '📉';
            default:
                return '➡️';
        }
    };

    const getTrendColor = (trend) => {
        switch (trend) {
            case 'increase':
                return 'text-red-600'; // Red for expense increase (bad)
            case 'decrease':
                return 'text-green-600'; // Green for expense decrease (good)
            default:
                return 'text-gray-600';
        }
    };

    const getBackgroundColor = (trend) => {
        switch (trend) {
            case 'increase':
                return 'bg-red-100'; // Red background for expense increase (bad)
            case 'decrease':
                return 'bg-green-100'; // Green background for expense decrease (good)
            default:
                return 'bg-gray-100';
        }
    };

    return (
        <div className='p-6 border border-gray-300 rounded-lg bg-white'>
            <p className='text-2xl font-bold mb-4'>Total Expenses: ${totalExpenses.toFixed(2)}</p>
            {expenseComparison && (
                <div className='mt-4 space-y-2'>
                    <div className='flex justify-between items-center'>
                        <span className='text-sm text-gray-600'>Expenses This Month:</span>
                        <span className='font-semibold'>${currentMonthExpenses.toFixed(2)}</span>
                    </div>
                    <div className='flex justify-between items-center'>
                        <span className='text-sm text-gray-600'>Previous Month:</span>
                        <span className='font-semibold'>${previousMonthExpenses.toFixed(2)}</span>
                    </div>
                    <div className={`p-2 ${getBackgroundColor(expenseComparison.trend)} rounded-lg`}>
                        <span>{getTrendIcon(expenseComparison.trend)}</span>
                        <span className={`font-semibold ${getTrendColor(expenseComparison.trend)}`}>
                            {expenseComparison.percentageChange > 0 ? '+' : ''}{expenseComparison.percentageChange.toFixed(1)}%
                        </span>
                    </div>
                </div>
            )}
        </div>
    )
}

