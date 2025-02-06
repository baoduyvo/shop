import React, {
  createContext,
  useState,
  useContext,
  ReactNode,
  Dispatch,
  SetStateAction,
} from 'react';

// Định nghĩa kiểu cho context
interface ResponseContextType {
  responseData: null | any; // Hoặc bạn có thể thay thế `any` bằng kiểu cụ thể bạn muốn
  setResponseData: Dispatch<SetStateAction<null | any>>; // Hàm để cập nhật state, sử dụng Dispatch
}

// Tạo Context với giá trị mặc định
const ResponseContext = createContext<ResponseContextType>({
  responseData: null, // Giá trị mặc định là null hoặc kiểu khởi tạo khác
  setResponseData: () => {}, // Hàm mặc định (hàm rỗng, nhưng sẽ được thay thế sau)
});

// Định nghĩa kiểu cho Props của ResponseProvider
interface ResponseProviderProps {
  children: ReactNode; // Chỉ định kiểu cho 'children' là ReactNode
}

// Tạo Provider để cung cấp context
export const ResponseProvider = ({ children }: ResponseProviderProps) => {
  const [responseData, setResponseData] = useState<null | any>(null); // Sử dụng `useState` với kiểu cụ thể

  return (
    <ResponseContext.Provider value={{ responseData, setResponseData }}>
      {children}
    </ResponseContext.Provider>
  );
};

// Hook để sử dụng context
export const useResponse = () => {
  return useContext(ResponseContext);
};
