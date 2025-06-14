

const TransactionContext = createContext()

export const TransactionProvider = ({ children }) => {
    const [transactions, setTransactions] = useState([])

    return (

        
        <TransactionContext.Provider value={{ transactions, setTransactions }}>
            {children}
        </TransactionContext.Provider>
    )
}