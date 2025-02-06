import React, { useState } from 'react';

import Breadcrumb from '../../../components/Breadcrumbs/Breadcrumb';
import SelectGroupOne from '../../../components/Forms/SelectGroup/SelectGroupOne';
import DatePickerOne from '../../../components/Forms/DatePicker/DatePickerOne';
import axios from 'axios';
import { Navigate } from 'react-router-dom';

const CreateUsers: React.FC = () => {
  const GenderOptions = ['MALE', 'FEMALE', 'OTHER'];
  const RoleOptions = ['USER', 'EMPLOYEE'];

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const [gender, setGender] = useState<String>('');
  const [role, setRole] = useState<string>('');
  const [address, setAddress] = useState('');
  const getGenderValue = (genderData: String) => {
    setGender(genderData);
  };
  const getRoleValue = (roleData: string) => {
    setRole(roleData);
  };
  const getDateValue = (date: Date | null) => {
    setSelectedDate(date);
  };
  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    const token = localStorage.getItem('items');
    const bearerToken = `Bearer ${token?.slice(1, -1)}`;
    try {
      const response = await axios.post(
        'http://localhost:8888/api/v1/jwt/users',
        {
          email: email,
          password: password,
          age: 1000000,
          gender: gender,
          address: address,
          role: role,
        },
        {
          headers: {
            Authorization: bearerToken,
          },
        },
      );
      console.log('Success:', response);
      if (response.data.statusCode === 200 && response.data.error === '99999') {
        // Navigate('/');
        console.log('thanh cong');
      }
    } catch (err) {
      console.error('Error:', err);
    } finally {
      console.log('Finally');
    }
  };
  return (
    <>
      <Breadcrumb pageName="Create User" />

      <div className="grid grid-cols-1 gap-9">
        <div className="flex flex-col gap-9">
          {/* <!-- Contact Form --> */}
          <div className="rounded-sm border border-stroke bg-white shadow-default dark:border-strokedark dark:bg-boxdark">
            <form onSubmit={handleSubmit}>
              <div className="p-6.5">
                <div className="mb-4.5">
                  <label className="mb-2.5 block text-black dark:text-white">
                    Email <span className="text-meta-1">*</span>
                  </label>
                  <input
                    type="email"
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    placeholder="Enter your email address"
                    className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                  />
                </div>

                <div className="mb-4.5">
                  <label className="mb-2.5 block text-black dark:text-white">
                    Password <span className="text-meta-1">*</span>
                  </label>
                  <input
                    type="password"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                    placeholder="Enter your email address"
                    className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                  />
                </div>

                <div className="mb-4.5">
                  <DatePickerOne getDateValue={getDateValue} />
                </div>

                <SelectGroupOne
                  title="Gender"
                  options={GenderOptions}
                  getGenderValue={getGenderValue}
                />

                <SelectGroupOne
                  title="Role"
                  options={RoleOptions}
                  getGenderValue={getRoleValue}
                />

                <div className="mb-6">
                  <label className="mb-2.5 block text-black dark:text-white">
                    Address
                  </label>
                  <textarea
                    rows={6}
                    value={address}
                    onChange={(event) => setAddress(event.target.value)}
                    placeholder="Type your message"
                    className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                  ></textarea>
                </div>

                <button
                  type="submit"
                  className="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90"
                >
                  Submit Users
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </>
  );
};

export default CreateUsers;
