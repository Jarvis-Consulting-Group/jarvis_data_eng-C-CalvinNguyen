import React from 'react'
import './Dashboard.scss'
import NavBar from "../../component/NavBar/NavBar";
import TraderList from "../../component/TraderList/TraderList";
import TraderListData from '../../component/TraderList/TraderListData.json'
import { Input, DatePicker, Modal, Button, Form } from "antd";
import axios from 'axios'
import { createTraderUrl, deleteTraderUrl, tradersUrl } from "../../util/constants";
import "antd/dist/reset.css"
import { useEffect, useState } from "react";

function Dashboard(props) {

  const [state, setState] = useState({
    isModalVisible: false,
    traders: []
  })

  const getTraders = async () => {
    const res = await axios.get(tradersUrl)
    if (res) {
      setState({
        ...state,
        traders: [...res.data] || []
      })
    }
  }

  const showModal = () => {
    setState({
      ...state,
      isModalVisible: true
    })
  }

  const handleOk = async () => {
    const paramUrl = `/firstname/${state.firstName}`
        + `/lastname/${state.lastName}`
        + `/dob/${state.dob}`
        + `/country/${state.country}`
        + `/email/${state.email}`
    const res = await axios.post(createTraderUrl + paramUrl, {}, {})
    const res2 = await axios.get(tradersUrl)

    if (res2) {
      setState({
        ...state,
        isModalVisible: false,
        firstName: null,
        lastName: null,
        dob: null,
        country: null,
        email: null,
        traders: [...res2.data] || []
      })
    } else {
      setState({
        ...state,
        isModalVisible: false,
        firstName: null,
        lastName: null,
        dob: null,
        country: null,
        email: null
      });
    }
  }

  const onInputChange = (field, value) => {
    setState({
      ...state,
      [field]: value
    })
  }

  const handleCancel = () => {
    setState({
      ...state,
      isModalVisible: false,
      firstName: null,
      lastName: null,
      dob: null,
      country: null,
      email: null
    })
  }

  useEffect(() => {
    getTraders()
  }, [])

  const onTraderDelete = async (id) => {
    const paramUrl = "/" + id
    const res = await axios.delete(deleteTraderUrl + paramUrl, {})
    await getTraders()
  }

  return (
      <div className="dashboard">
        <div className="title">
          Dashboard
          <div className="add-trader-button">

            <Button onClick={showModal}>Add New Trader</Button>

            <Modal title="Add New Trader" okText="Submit"
                   open={state.isModalVisible}
                   onOk={handleOk} onCancel={handleCancel}>

              <Form layout="vertical">

                <div className="add-trader-form">
                  <div className="add-trader-field">
                    <Form.Item label="First Name">
                      <Input allowClear={false} placeholder="John"
                             value={state.firstName}
                             onChange={(event) => onInputChange(
                                 "firstName", event.target.value)} />
                    </Form.Item>
                  </div>
                  <div className="add-trader-field">
                    <Form.Item label="Last Name">
                      <Input allowClear={false} placeholder="Doe"
                             value={state.lastName}
                             onChange={(event) => onInputChange(
                                 "lastName", event.target.value)} />
                    </Form.Item>
                  </div>
                  <div className="add-trader-field">
                    <Form.Item label="Email">
                      <Input allowClear={false} placeholder="test@email.com"
                             value={state.email}
                             onChange={(event) => onInputChange(
                                 "email", event.target.value)} />
                    </Form.Item>
                  </div>
                  <div className="add-trader-field">
                    <Form.Item label="Country">
                      <Input allowClear={false} placeholder="Canada"
                             value={state.country}
                             onChange={(event) => onInputChange(
                                 "country", event.target.value)} />
                    </Form.Item>
                  </div>
                  <div className="add-trader-field">
                    <Form.Item label="Date of Birth">
                      <DatePicker style={{width:"100%"}} placeholder=""
                                  onChange={(date, dateString) => {
                                    if (date != null) { onInputChange(
                            "dob", date.format("YYYY-MM-DD"))}}} />
                    </Form.Item>
                  </div>
                </div>

              </Form>
            </Modal>
          </div>
        </div>

        <NavBar />

        <div className="dashboard-content">
          <TraderList onTraderDeleteClick={onTraderDelete}
                      traders={state.traders}/>
        </div>
      </div>
  )
}

export default Dashboard