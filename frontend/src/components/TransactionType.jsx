import React from 'react';
import { ExpensePieChart } from './ExpensePieChart';
import { IncomePieChart } from './IncomePieChart';

export const TransactionType = () => {
  
  return (
    <div>
      <h1 className="text-2xl font-bold mb-3">Income and Expense Type</h1>
      <div className='grid grid-cols-2 gap-4'>
        <ExpensePieChart/>
        <IncomePieChart/>
      </div>
    </div>
  )
}
