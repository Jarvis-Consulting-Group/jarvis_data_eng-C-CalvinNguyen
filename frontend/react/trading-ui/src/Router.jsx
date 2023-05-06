import { BrowserRouter, Route, Routes } from "react-router-dom";
import Dashboard from './page/Dashboard/Dashboard'
import QuotePage from "./page/QuotePage/QuotePage";
import TraderAccountPage from "./page/TraderAccountPage/TraderAccountPage";

export default function Router() {
  return (
      <BrowserRouter>
        <Routes>
          <Route exact path="/"
                 element={<Dashboard />} />
          <Route exact path="/dashboard"
                 element={<Dashboard />} />
          <Route exact path="/traders"
                 element={<Dashboard />} />
          <Route exact path="/quotes"
                 element={<QuotePage />} />
          <Route exact path="/trader/:traderId"
                 element={<TraderAccountPage />} />
        </Routes>
      </BrowserRouter>
  )
}
