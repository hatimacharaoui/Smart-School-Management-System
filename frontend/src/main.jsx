import React from "react";
import ReactDOM from 'react-dom/client'
import {AuthProvider} from "./context/AuthContext.jsx";
import './styles.css'
import App from './App.jsx'
import {BrowserRouter} from "react-router-dom";

ReactDOM.createRoot(document.getElementById('root')).render(

      <BrowserRouter future={{ v7_startTransition: true, v7_relativeSplatPath: true,}}>
          <AuthProvider>
              <App />
          </AuthProvider>
      </BrowserRouter>,
)
