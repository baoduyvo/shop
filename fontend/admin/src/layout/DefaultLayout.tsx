import React, { useState, ReactNode, useEffect } from 'react';
import Header from '../components/Header/index';
import Sidebar from '../components/Sidebar/index';
import { toast, ToastContainer, Bounce } from 'react-toastify';
import axios from 'axios';
import { useResponse } from '../hooks/ResponseProvider';

const DefaultLayout: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { setResponseData } = useResponse();
  useEffect(() => {
    const fetchData = async () => {
      const token = localStorage.getItem('items');
      const bearerToken = `Bearer ${token?.slice(1, -1)}`;
      try {
        const response = await axios.post(
          'http://localhost:8888/api/v1/jwt/auth/introspect',
          {
            token: token?.slice(1, -1),
          },
          {
            headers: {
              Authorization: bearerToken,
            },
          },
        );

        if (
          response.data.statusCode === 200 &&
          response.data.error === '99999'
        ) {
          setResponseData(response.data);
        }
      } catch (err) {
        console.error('Error:', err);
      } finally {
        console.log('Finally');
      }
    };

    fetchData();
  }, []);
  return (
    <>
      <div className="dark:bg-boxdark-2 dark:text-bodydark">
        {/* <!-- ===== Page Wrapper Start ===== --> */}
        <div className="flex h-screen overflow-hidden">
          {/* <!-- ===== Sidebar Start ===== --> */}
          <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />
          {/* <!-- ===== Sidebar End ===== --> */}

          {/* <!-- ===== Content Area Start ===== --> */}
          <div className="relative flex flex-1 flex-col overflow-y-auto overflow-x-hidden">
            {/* <!-- ===== Header Start ===== --> */}
            <Header sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />
            <ToastContainer
              position="top-center"
              autoClose={1000}
              hideProgressBar={false}
              newestOnTop={false}
              closeOnClick={false}
              rtl={false}
              pauseOnFocusLoss
              draggable
              pauseOnHover
              theme="dark"
              transition={Bounce}
              style={{
                position: 'absolute',
                right: '0px',
                top: '0px',
                zIndex: 50,
              }}
            />
            {/* <!-- ===== Header End ===== --> */}

            {/* <!-- ===== Main Content Start ===== --> */}
            <main>
              <div className="mx-auto max-w-screen-2xl p-4 md:p-6 2xl:p-10">
                {children}
              </div>
            </main>
            {/* <!-- ===== Main Content End ===== --> */}
          </div>
          {/* <!-- ===== Content Area End ===== --> */}
        </div>
        {/* <!-- ===== Page Wrapper End ===== --> */}
      </div>
    </>
  );
};

export default DefaultLayout;
