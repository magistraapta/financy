import React, { useState, useEffect } from 'react'
import { useAuth } from './context/AuthContext';
import axios from 'axios';

export const TotalIncome = () => {
    const [totalIncome, setTotalIncome] = useState(0);
    const [currentMonthIncome, setCurrentMonthIncome] = useState(0);
    const [previousMonthIncome, setPreviousMonthIncome] = useState(0);
    const [incomeComparison, setIncomeComparison] = useState(null);
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
        } catch (error) {
            setError('Error fetching total income: ' + error.message);
        } finally {
            setLoading(false);
        }
    }

    const handleIncomeComparison = async () => {
        try {
            const user = getUser();
            if (!user || !user.username || !user.password) {
                throw new Error('User not authenticated or missing credentials');
            }
            const response = await axios.get('http://localhost:8080/transactions/income-comparison', {
                headers: {
                    'Authorization': 'Basic ' + btoa(`${user.username}:${user.password}`),
                    'Content-Type': 'application/json'
                }
            });
            setIncomeComparison(response.data);
            setCurrentMonthIncome(response.data.currentMonthIncome);
            setPreviousMonthIncome(response.data.previousMonthIncome);
        } catch (error) {
            console.error('Error fetching income comparison:', error);
        }
    }

    useEffect(() => {
        handleTotalIncome();
        handleIncomeComparison();
    }, []);

    useEffect(() => {
        const handleTransactionAdded = () => {
            handleTotalIncome();
            handleIncomeComparison();
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
                return 'text-green-600';
            case 'decrease':
                return 'text-red-600';
            default:
                return 'text-gray-600';
        }
    };

    return (
        <div className='p-6 border border-gray-300 rounded-lg bg-white'>
            <p className='text-2xl font-bold mb-4'>Total Income: ${totalIncome.toFixed(2)}</p>
            
            {incomeComparison && (
                <div className='mt-4 space-y-2'>
                    <div className='flex justify-between items-center'>
                        <span className='text-sm text-gray-600'>Income This Month:</span>
                        <span className='font-semibold'>${currentMonthIncome.toFixed(2)}</span>
                    </div>
                    <div className='flex justify-between items-center'>
                        <span className='text-sm text-gray-600'>Previous Month:</span>
                        <span className='font-semibold'>${previousMonthIncome.toFixed(2)}</span>
                    </div>
                    <div className={`p-2 ${incomeComparison.trend === 'increase' ? 'bg-green-100' : 'bg-red-100'} rounded-lg`}>
                            <span>{getTrendIcon(incomeComparison.trend)}</span>
                            <span className={`font-semibold ${getTrendColor(incomeComparison.trend)}`}>
                                {incomeComparison.percentageChange > 0 ? '+' : '-'}{incomeComparison.percentageChange.toFixed(1)}%
                            </span>
                    </div>
                </div>
            )}
        </div>
    )
}
