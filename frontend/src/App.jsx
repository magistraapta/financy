import { Routes, Route, useLocation } from 'react-router-dom'
import { SignedIn, SignedOut, UserButton } from '@clerk/clerk-react'
import LoginPage from './pages/LoginPage'
import { HomePage } from './pages/HomePage'
import LandingPage from './pages/LandingPage'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'

function App() {
  const location = useLocation()
  const isLoginPage = location.pathname === '/login'

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navigation Bar */}
      {!isLoginPage && (
        <nav className="bg-white shadow-sm border-b border-gray-200">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between h-16">
              <div className="flex items-center">
                <h1 className="text-2xl font-bold text-gray-900">Financy</h1>
              </div>
              <div className="flex items-center space-x-4">
                <SignedIn>
                  <UserButton 
                    appearance={{
                      elements: {
                        avatarBox: "w-8 h-8"
                      }
                    }}
                  />
                </SignedIn>
                <SignedOut>
                  <a 
                    href="/login" 
                    className="text-blue-600 hover:text-blue-800 font-medium"
                  >
                    Sign In
                  </a>
                </SignedOut>
              </div>
            </div>
          </div>
        </nav>
      )}

      <main className="w-full px-4 sm:px-6 lg:px-8 py-6">
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
        </Routes>
      </main>
      <ToastContainer />
    </div>
  )
}

export default App
