import React, { useEffect } from 'react'
import { Income } from '../components/Income'
import { Expense } from '../components/Expense'
import { TransactionType } from '../components/TransactionType'
import { TotalIncome } from '../components/TotalIncome'
import { TotalExpenses } from '../components/TotalExpenses'
import { useAuth } from '../components/context/AuthContext'
import { useNavigate } from 'react-router-dom'

export const HomePage = () => {
    const { userIsAuthenticated } = useAuth()
    const navigate = useNavigate()

    useEffect(() => {
        if (!userIsAuthenticated()) {
            navigate('/login')
        }
    }, [userIsAuthenticated, navigate])

    if (!userIsAuthenticated()) {
        return null
    }

    return (
        <div className='gap-y-4 grid'>
            <div className='grid grid-cols-2 gap-4 items-center justify-center'>
                <TotalIncome/>
                <TotalExpenses/>
            </div>
            <div className="grid grid-cols-2 gap-4 items-center justify-center min-h-[300px]">
                <div className='border border-gray-300 rounded-lg min-h-full p-6 bg-white'>
                    <Income />
                </div>
                <div className='border border-gray-300 rounded-lg min-h-full p-6 bg-white'>
                    <Expense/>  
                </div>
            </div>
            <div className='border border-gray-300 rounded-lg min-h-full h-[600px] p-6 bg-white'>
                <TransactionType/>
            </div>
        </div>
        
    )
} 