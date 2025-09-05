import React from 'react'
import { Income } from '../components/Income'
import { Expense } from '../components/Expense'
import { TransactionType } from '../components/TransactionType'
import { TotalIncome } from '../components/TotalIncome'
import { TotalExpenses } from '../components/TotalExpenses'
import { SignedIn, SignedOut, RedirectToSignIn } from '@clerk/clerk-react'

export const HomePage = () => {
    return (
        <>
            <SignedOut>
                <RedirectToSignIn />
            </SignedOut>
            <SignedIn>
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
            </SignedIn>
        </>
    )
} 