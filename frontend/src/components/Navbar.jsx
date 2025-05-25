import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from './context/AuthContext'
import { useState } from 'react'
import { TransactionModal } from './TransactionModal'

export function Navbar() {
  const { userIsAuthenticated, userLogout, getUser } = useAuth()
  const navigate = useNavigate()
  const [isModalOpen, setIsModalOpen] = useState(false)

  const handleLogout = () => {
    userLogout()
    navigate('/login')
  }

  const handleAddTransaction = (transactionData) => {
    // TODO: Implement transaction submission logic
    console.log('New transaction:', transactionData)
  }

  const user = getUser()

  return (
    <nav className="bg-white shadow-sm">
      <div className="px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">

          {/* Logo */}
          <div className="flex items-center">
            <Link to="/">
              <h1 className="text-2xl font-bold">Financy</h1>
            </Link>
          </div>

          {/* User Info */}
          <div className="flex items-center space-x-4">
            <div className="flex items-center text-gray-600 gap-2">
            <span className="material-symbols-outlined">person</span>
              <span className="text-sm">{userIsAuthenticated() ? user?.username : 'Guest'}</span>
            </div>

            {/* Add Transaction Button */}
            <div>
              <button
                onClick={() => setIsModalOpen(true)}
                disabled={!userIsAuthenticated()}
                className="text-white bg-blue-700 px-3 py-2 rounded-md text-sm font-medium disabled:opacity-50 disabled:cursor-not-allowed hover:bg-blue-800"
              >
                Add Transaction
              </button>
            </div>

            {/* Logout Button */}
            {userIsAuthenticated() ? (
              <button
                onClick={handleLogout}
                className="text-gray-600 border hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
              >
                Logout
              </button>
            ) : (
              <Link 
                to="/login" 
                className="text-white border bg-blue-600 hover:bg-blue-800 px-3 py-2 rounded-md text-sm font-medium"
              >
                Login
              </Link>
            )}
          </div>
        </div>
      </div>

      <TransactionModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleAddTransaction}
      />
    </nav>
  )
} 