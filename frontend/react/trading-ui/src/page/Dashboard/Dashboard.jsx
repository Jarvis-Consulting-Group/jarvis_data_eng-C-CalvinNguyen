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

  const [form] = Form.useForm()
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
    if (state.firstName == null || state.lastName ||
        state.dob || state.country || state.email) {
      return
    }

    const paramUrl = `/firstname/${state.firstName}`
        + `/lastname/${state.lastName}`
        + `/dob/${state.dob}`
        + `/country/${state.country}`
        + `/email/${state.email}`
    const res = await axios.post(createTraderUrl + paramUrl, {}, {})
    const res2 = await axios.get(tradersUrl)

    if (res2) {
      form.resetFields()
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
      form.resetFields()
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
    form.resetFields()
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

              <Form layout="vertical" form={form}>

                <div className="add-trader-form">
                  <div className="add-trader-field">
                    <Form.Item
                        name="First Name"
                        label="First Name"
                        rules={[{
                          required: true,
                          message: "First Name requires an input."
                        }]}
                    >
                      <Input allowClear={false} placeholder="John"
                             value={state.firstName}
                             onChange={(event) => onInputChange(
                                 "firstName", event.target.value)} />
                    </Form.Item>
                  </div>
                  <div className="add-trader-field">
                    <Form.Item
                        name="Last Name"
                        label="Last Name"
                        rules={[{
                          required: true,
                          message: "Last Name requires an input."
                        }]}
                    >
                      <Input allowClear={false} placeholder="Doe"
                             value={state.lastName}
                             onChange={(event) => onInputChange(
                                 "lastName", event.target.value)} />
                    </Form.Item>
                  </div>
                  <div className="add-trader-field">
                    <Form.Item
                        name="Email"
                        label="Email"
                        rules={[{
                          required: true,
                          message: "A valid email address must be entered.",
                          pattern: new RegExp(/^[a-zA-Z0-9]+@[a-zA-Z0-9-]+.[a-zA-Z]+$/)
                        }]}
                    >
                      <Input allowClear={false} placeholder="test@email.com"
                             value={state.email}
                             onChange={(event) => onInputChange(
                                 "email", event.target.value)} />
                    </Form.Item>
                  </div>
                  <div className="add-trader-field">
                    <Form.Item
                        name="Country"
                        label="Country"
                        rules={[{
                          required: true,
                          message: "A valid country must be entered"
                        }]}
                    >
                      <Input allowClear={false} placeholder="Canada"
                             value={state.country}
                             onChange={(event) => onInputChange(
                                 "country", event.target.value)} />
                    </Form.Item>
                  </div>
                  <div className="add-trader-field">
                    <Form.Item
                        name="Date of Birth"
                        label="Date of Birth"
                        rules={[{
                          type: 'object',
                          required: true,
                          message: "Please select a valid date."
                        }]}
                    >
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