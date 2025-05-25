import React from 'react'

const Footer = () => {
  return (
    <footer className="bg-blue-800 border-t border-gray-200 py-8 w-full ">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col md:flex-row justify-between items-center gap-8 md:gap-4">
          <div className="flex flex-col gap-2 text-center md:text-left">
            <h3 className="text-xl font-semibold text-white">Financy</h3>
            <p className="text-sm text-white">Your personal finance companion</p>
          </div>
          <div className="text-center md:text-left">
            <p className="text-sm text-white">
              &copy; {new Date().getFullYear()} Financy. All rights reserved.
            </p>
          </div>
        </div>
      </div>
    </footer>
  )
}

export default Footer