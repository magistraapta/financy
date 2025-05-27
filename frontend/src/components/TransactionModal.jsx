import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useAuth } from './context/AuthContext';

export const TransactionModal = ({ isOpen, onClose, onSubmit }) => {
  const [formData, setFormData] = useState({
    amount: 0,
    type: 'INCOME',
    category: 'SALARY',
    date: new Date().toISOString().split('T')[0],
  });
  const { getUser } = useAuth();

  const incomeCategories = [
    { value: 'SALARY', label: 'Salary' },
    { value: 'FREELANCE', label: 'Freelance' },
    { value: 'INVESTMENT', label: 'Investment' },
    { value: 'BUSINESS', label: 'Business' },
    { value: 'OTHER_INCOME', label: 'Other Income' }
  ];

  const expenseCategories = [
    { value: 'FOOD', label: 'Food' },
    { value: 'TRANSPORTATION', label: 'Transportation' },
    { value: 'HOUSING', label: 'Housing' },
    { value: 'UTILITIES', label: 'Utilities' },
    { value: 'ENTERTAINMENT', label: 'Entertainment' },
    { value: 'SHOPPING', label: 'Shopping' },
    { value: 'HEALTHCARE', label: 'Healthcare' },
    { value: 'EDUCATION', label: 'Education' },
    { value: 'TRAVEL', label: 'Travel' },
    { value: 'OTHER_EXPENSE', label: 'Other Expense' }
  ];

  // Update category when type changes
  useEffect(() => {
    setFormData(prev => ({
      ...prev,
      category: prev.type === 'INCOME' ? 'SALARY' : 'FOOD'
    }));
  }, [formData.type]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const user = getUser();
      if (!user || !user.username || !user.password) {
        toast.error('User not authenticated. Please login again.');
        return;
      }

      // Validate form data
      if (!formData.amount || formData.amount <= 0) {
        toast.error('Please enter a valid amount');
        return;
      }

      // Prepare request body with user information
      const requestBody = {
        amount: formData.amount,
        type: formData.type,
        category: formData.category,
        date: formData.date,
        user: {
          id: user.id
        }
      };

      const response = await fetch('http://localhost:8080/transactions', {
        method: 'POST',
        headers: {
          'Authorization': 'Basic ' + btoa(`${user.username}:${user.password}`),
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Failed to add transaction: ${response.status}`);
      }

      const data = await response.json();
      console.log('Transaction added:', data);

      // Call onSubmit with the form data
      onSubmit(formData);

      // Dispatch custom event to notify components about new transaction
      window.dispatchEvent(new Event('transactionAdded'));
      
      // Force a page reload to ensure all data is refetched
    //   window.location.reload();

      toast.success('Transaction added successfully!', {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
      });

      setFormData({
        amount: '',
        type: 'INCOME',
        date: new Date().toISOString().split('T')[0],
        description: ''
      });

      onClose();
    } catch (error) {
      console.error('Error adding transaction:', error);
      toast.error(error.message || 'Failed to add transaction. Please try again.');
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 backdrop-blur-sm bg-black/30 flex items-center justify-center z-50">
      <div className="bg-white/90 backdrop-blur-md rounded-lg p-8 w-full max-w-md shadow-xl">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold">Add Transaction</h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700"
          >
            ✕
          </button>
        </div>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="amount" className="block text-sm font-medium text-gray-700">
              Amount
            </label>
            <input
              type="number"
              id="amount"
              name="amount"
              value={formData.amount}
              onChange={handleChange}
              required
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white/80"
            />
          </div>

          <div>
            <label htmlFor="type" className="block text-sm font-medium text-gray-700">
              Type
            </label>
            <select
              id="type"
              name="type"
              value={formData.type}
              onChange={handleChange}
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white/80"
            >
              <option value="INCOME">Income</option>
              <option value="EXPENSE">Expense</option>
            </select>
          </div>

          <div>
            <label htmlFor="category" className="block text-sm font-medium text-gray-700">
              Category
            </label>
            <select
              id="category"
              name="category"
              value={formData.category}
              onChange={handleChange}
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white/80"
            >
              {formData.type === 'INCOME' ? (
                incomeCategories.map(category => (
                  <option key={category.value} value={category.value}>
                    {category.label}
                  </option>
                ))
              ) : (
                expenseCategories.map(category => (
                  <option key={category.value} value={category.value}>
                    {category.label}
                  </option>
                ))
              )}
            </select>
          </div>

          <div>
            <label htmlFor="date" className="block text-sm font-medium text-gray-700">
              Date
            </label>
            <input
              type="date"
              id="date"
              name="date"
              value={formData.date}
              onChange={handleChange}
              required
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white/80"
            />
          </div>

          <div className="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50 bg-white/80"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600/90 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Add Transaction
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}; 