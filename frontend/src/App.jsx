import { useEffect, useState } from 'react'
import './App.css'

function App() {
  const [data, setData] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchAuthTest = async () => {
    try {
      setLoading(true);
      const response = await fetch('http://localhost:8080/auth/test');
      const text = await response.text();
      setData(text);
      setError(null);
    } catch (error) {
      console.error('Error fetching auth test:', error);
      setError('Failed to fetch data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAuthTest();
  }, []);

  return (
    <>
      <div>
        {loading && <p>Loading...</p>}
        {error && <p style={{ color: 'red' }}>{error}</p>}
        {!loading && !error && <p>{data}</p>}
      </div>
    </>
  )
}

export default App
