import React from 'react'
import { SignedIn, SignedOut, SignInButton, UserButton, RedirectToSignIn } from '@clerk/clerk-react'
import { useNavigate } from 'react-router-dom'

export default function LoginPage() {
  const navigate = useNavigate()

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <div className="text-center">
          <h1 className="text-6xl font-bold text-gray-900 mb-2">Financy</h1>
          <p className="text-xl text-gray-600 mb-8">Your Personal Finance Manager</p>
        </div>
        
        <div className="bg-white py-8 px-6 shadow-xl rounded-lg">
          <div className="text-center mb-8">
            <h2 className="text-3xl font-bold text-gray-900">Welcome Back</h2>
            <p className="mt-2 text-sm text-gray-600">
              Sign in to manage your finances
            </p>
          </div>

          <SignedOut>
            <div className="space-y-6">
              <SignInButton 
                mode="modal"
                redirectUrl="/home"
                className="w-full flex justify-center py-3 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition duration-150 ease-in-out"
              >
                <span className="text-lg">Sign In</span>
              </SignInButton>
              
              <div className="text-center">
                <p className="text-sm text-gray-600">
                  Don't have an account? Sign up is included with sign in.
                </p>
              </div>
            </div>
          </SignedOut>

          <SignedIn>
            <div className="text-center space-y-6">
              <div className="flex justify-center">
                <UserButton 
                  appearance={{
                    elements: {
                      avatarBox: "w-16 h-16"
                    }
                  }}
                />
              </div>
              <div>
                <h3 className="text-lg font-medium text-gray-900">You're signed in!</h3>
                <p className="text-sm text-gray-600">Welcome to Financy</p>
              </div>
              <button
                onClick={() => navigate('/home')}
                className="w-full flex justify-center py-3 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition duration-150 ease-in-out"
              >
                Go to Dashboard
              </button>
            </div>
          </SignedIn>
        </div>

        <div className="text-center">
          <p className="text-xs text-gray-500">
            Secure authentication powered by Clerk
          </p>
        </div>
      </div>
    </div>
  )
}