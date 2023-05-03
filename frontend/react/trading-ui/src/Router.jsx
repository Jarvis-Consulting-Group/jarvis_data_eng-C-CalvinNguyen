import { BrowserRouter, Route, Routes } from "react-router-dom";
import Dashboard from './page/Dashboard/Dashboard'

export default function Router() {
  return (
      <BrowserRouter>
        <Routes>
          <Route exact path="/" element={<Dashboard />} />
          <Route exact path="/dashboard" element={<Dashboard />} />
        </Routes>
      </BrowserRouter>
  )
}
