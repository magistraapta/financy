import React from 'react'
import { Income } from '../components/Income'
import { Expense } from '../components/Expense'
import { TransactionType } from '../components/TransactionType'

export const HomePage = () => {
    return (
        <div className='gap-y-4 grid'>
            <div className='grid grid-cols-2 gap-4 items-center justify-center'>
                <div className='p-6 border border-gray-300 rounded-lg bg-white'>
                    <p className='text-2xl font-bold'>Total Income: 20000</p>
                </div>
                <div className='p-6 border border-gray-300 rounded-lg bg-white'>
                    <p className='text-2xl font-bold'>Total Expense: 10000</p>
                </div>
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