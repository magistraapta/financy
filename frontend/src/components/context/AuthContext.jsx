import { createContext, useContext, useState, useEffect } from 'react'

const defaultContextValue = {
    user: null,
    getUser: () => null,
    userIsAuthenticated: () => false,
    userLogin: () => {},
    userLogout: () => {}
}

const AuthContext = createContext(defaultContextValue)

function AuthProvider({ children }) {
    const [user, setUser] = useState(null)

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem('user'))
        if (storedUser) {
            setUser(storedUser)
        }
    }, [])

    const getUser = () => {
        return JSON.parse(localStorage.getItem('user'))
    }

    const userIsAuthenticated = () => {
        return localStorage.getItem('user') !== null
    }

    const userLogin = (user) => {
        localStorage.setItem('user', JSON.stringify(user))
        setUser(user)
    }

    const userLogout = () => {
        localStorage.removeItem('user')
        setUser(null)
    }

    const contextValue = {
        user,
        getUser,
        userIsAuthenticated,
        userLogin,
        userLogout
    }
    
    return <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>
}

export function useAuth() {
    const context = useContext(AuthContext)
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider')
    }
    return context
}

export { AuthProvider } 