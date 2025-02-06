import React from 'react';
import ReactDOM from 'react-dom/client';
import {
  BrowserRouter,
  Route,
  BrowserRouter as Router,
  Routes,
} from 'react-router-dom';
import App from './App';
import './css/style.css';
import './css/satoshi.css';
import 'jsvectormap/dist/css/jsvectormap.css';
import 'flatpickr/dist/flatpickr.min.css';
import SignIn from './pages/Authentication/SignIn';
import Register from './pages/Authentication/Register';
import { ResponseProvider } from './hooks/ResponseProvider';

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <ResponseProvider>
      <BrowserRouter>
        <div className="App" style={{ backgroundImage: 'url()' }}>
          <Routes>
            <Route
              path="/login"
              element={
                <>
                  <SignIn />
                </>
              }
            />
            <Route
              path="/register"
              element={
                <>
                  <Register />
                </>
              }
            />
            <Route path="*" element={<App />}></Route>
          </Routes>
        </div>
        {/* <App /> */}
      </BrowserRouter>
    </ResponseProvider>
  </React.StrictMode>,
);
