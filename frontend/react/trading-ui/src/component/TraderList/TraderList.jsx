import React from "react";
import { Table } from "antd";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faTrashAlt as deleteIcon,
  faMagnifyingGlass as traderIcon
} from '@fortawesome/free-solid-svg-icons'
import 'antd/dist/reset.css'
import './TraderList.scss'
import { useState, useEffect } from "react";
import TraderListData from './TraderListData.json'
import { useNavigate } from "react-router-dom";

function TraderList(props) {

  let navigate = useNavigate()

  const routeTrader = (traderId) => {
    let path = "/trader/"
    navigate(path + traderId)
  }

  const columns = [
    {
      title: 'First Name',
      dataIndex: 'firstName',
      key: 'firstName'
    },
    {
      title: 'Last Name',
      dataIndex: 'lastName',
      key: 'lastName'
    },
    {
      title: 'Date of Birth',
      dataIndex: 'dob',
      key: 'dob'
    },
    {
      title: 'Country',
      dataIndex: 'country',
      key: 'country'
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email'
    },
    {
      title: 'Actions',
      dataIndex: 'actions',
      key: 'actions',
      render: (text, record) => (
          <>
          <div className="trader-delete-icon">
            <FontAwesomeIcon icon={ deleteIcon } onClick={() => props.onTraderDeleteClick(record.id)} />
          </div>
          <div className="trader-icon">
            <FontAwesomeIcon icon={ traderIcon } onClick={() => routeTrader(record.id)} />
          </div>
          </>
      )
    },
  ]

  const [tableColumns, setTableColumns] = useState(columns)

  return (
      <Table
          dataSource={props.traders}
          columns={tableColumns}
          pagination={false}
      />
  )
}

export default TraderList