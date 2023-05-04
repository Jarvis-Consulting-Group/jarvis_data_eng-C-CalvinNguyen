import React from "react";
import './QuotePage.scss'
import { useState, useEffect } from "react";
import axios from "axios";
import { dailyListQuotesUrl } from "../../util/constants";
import QuoteList from "../../component/QuoteList/QuoteList";
import NavBar from "../../component/NavBar/NavBar";

function QuotePage(props) {

  const [state, setState] = useState({
    quotes: []
  })

  const getQuotes = async () => {
    const res = await axios.get(dailyListQuotesUrl)
    if (res) {
      setState({
        ...state,
        quotes: [...res.data] || []
      })
    }
  }

  useEffect(() => {
    getQuotes();
  }, [])

  return (
      <div className="quote-page">
        <div className="title">
          Dashboard
        </div>

        <NavBar />

        <div className="dashboard-content">
          <QuoteList quotes={state.quotes}/>
        </div>
      </div>
  )

}

export default QuotePage