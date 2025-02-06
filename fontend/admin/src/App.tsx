import { useEffect, useState } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';

import Loader from './common/Loader';
import PageTitle from './components/PageTitle';
import ECommerce from './pages/Admin/Dashboard/ECommerce';
import Settings from './pages/Admin/Settings';
import DefaultLayout from './layout/DefaultLayout';
import CreateUsers from './pages/Admin/Users/Create';
import ListUsers from './pages/Admin/Users/List';
import SignIn from './pages/Authentication/SignIn';

function App() {
  const [loading, setLoading] = useState<boolean>(true);
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  useEffect(() => {
    setTimeout(() => setLoading(false), 1000);
  }, []);

  return loading ? (
    <Loader />
  ) : (
    <DefaultLayout>
      <Routes>
        <Route
          index
          path="/"
          element={
            <>
              <PageTitle title="eCommerce Dashboard | TailAdmin - Tailwind CSS Admin Dashboard Template" />
              <ECommerce />
            </>
          }
        />
        <Route
          path="/user/create"
          element={
            <>
              <PageTitle title="Create User" />
              <CreateUsers />
            </>
          }
        />
        <Route
          path="/user/List"
          element={
            <>
              <PageTitle title="List User" />
              <ListUsers />
            </>
          }
        />
        <Route
          path="/user/detail/:id"
          element={
            <>
              <PageTitle title="Detail User" />
              <ListUsers />
            </>
          }
        />

        <Route
          path="/settings"
          element={
            <>
              <PageTitle title="Settings | TailAdmin - Tailwind CSS Admin Dashboard Template" />
              <Settings />
            </>
          }
        />

        {/* <Route
          path="/login"
          element={
            <>
              <PageTitle title="Login" />
              <SignIn />
            </>
          }
        /> */}
      </Routes>
    </DefaultLayout>
  );
}

export default App;
