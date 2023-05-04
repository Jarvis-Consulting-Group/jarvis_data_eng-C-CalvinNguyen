import React from "react";
import { Table } from "antd";
import 'antd/dist/reset.css'
import { useState, useEffect } from "react";

function QuoteList(props) {

  const columns = [
    {
      title: 'Ticker',
      dataIndex: 'ticker',
      key: 'ticker'
    },
    {
      title: 'Last Price',
      dataIndex: 'lastPrice',
      key: 'lastPrice'
    },
    {
      title: 'Bid Price',
      dataIndex: 'bidPrice',
      key: 'bidPrice'
    },
    {
      title: 'Bid Size',
      dataIndex: 'bidSize',
      key: 'bidSize'
    },
    {
      title: 'Ask Price',
      dataIndex: 'askPrice',
      key: 'askPrice'
    },
    {
      title: 'Ask Size',
      dataIndex: 'askSize',
      key: 'askSize',
    }
  ]

  const [tableColumns, setTableColumns] = useState(columns)

  return (
      <Table
          dataSource={props.quotes}
          columns={tableColumns}
          pagination={false}
      />
  )
}

export default QuoteList