import React from 'react'
import { Link } from 'react-router-dom'
import { SignedIn, SignedOut } from '@clerk/clerk-react'

const LandingPage = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
      <div className="max-w-4xl mx-auto text-center px-4 sm:px-6 lg:px-8">
        <div className="mb-12">
          <h1 className="text-6xl font-bold text-gray-900 mb-6">
            Welcome to <span className="text-blue-600">Financy</span>
          </h1>
          <p className="text-xl text-gray-600 mb-8 max-w-2xl mx-auto">
            Take control of your finances with our intuitive personal finance management tool. 
            Track income, expenses, and gain insights into your spending habits.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-12">
          <div className="bg-white p-6 rounded-lg shadow-md">
            <div className="text-blue-600 text-4xl mb-4">💰</div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">Track Income</h3>
            <p className="text-gray-600">Monitor your income sources and financial growth</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow-md">
            <div className="text-blue-600 text-4xl mb-4">📊</div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">Analyze Expenses</h3>
            <p className="text-gray-600">Categorize and analyze your spending patterns</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow-md">
            <div className="text-blue-600 text-4xl mb-4">📈</div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">Visualize Data</h3>
            <p className="text-gray-600">Beautiful charts and graphs for better insights</p>
          </div>
        </div>

        <div className="space-y-4">
          <SignedOut>
            <Link
              to="/login"
              className="inline-flex items-center px-8 py-4 border border-transparent text-lg font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition duration-150 ease-in-out"
            >
              Get Started
            </Link>
          </SignedOut>
          <SignedIn>
            <Link
              to="/home"
              className="inline-flex items-center px-8 py-4 border border-transparent text-lg font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition duration-150 ease-in-out"
            >
              Go to Dashboard
            </Link>
          </SignedIn>
        </div>
      </div>
    </div>
  )
}

export default LandingPage