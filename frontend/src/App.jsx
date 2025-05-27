import { Routes, Route, useLocation } from 'react-router-dom'
import { AuthProvider } from './components/context/AuthContext'
import { LoginPage } from './pages/LoginPage'
import { HomePage } from './pages/HomePage'
import { Navbar } from './components/Navbar'
import Footer from './components/Footer'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'

function App() {
  const location = useLocation()
  const isLoginPage = location.pathname === '/login'

  return (
    <AuthProvider>
      <div className="min-h-screen bg-gray-50">
        {!isLoginPage && <Navbar />}
        <main className="w-full px-4 sm:px-6 lg:px-8 py-6">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />
          </Routes>
        </main>
        {!isLoginPage && <Footer />}
        <ToastContainer />
      </div>
    </AuthProvider>
  )
}

export default App
