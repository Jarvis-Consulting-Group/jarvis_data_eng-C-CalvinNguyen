import React, { useEffect, useState } from 'react'
import './TraderAccountPage.scss'
import { traderAccountUrl, withdrawFundsUrl,
  depositFundsUrl } from "../../util/constants"
import 'antd/dist/reset.css'
import { useParams } from "react-router-dom";
import { Input, Modal, Button, Form } from "antd";
import axios from 'axios'
import NavBar from "../../component/NavBar/NavBar";

function TraderAccountPage() {

  const [form] = Form.useForm()
  const routeParams = useParams()

  const [state, setState] = useState({
    isWithdrawModalVisible: false,
    isDepositModalVisible: false,
    depositFunds: null,
    withdrawFunds: null,
    traderAccount: {
      account: {},
      trader: {}
    }
  })

  const fetchTrader = async (traderId) => {
    const res = await axios.get(traderAccountUrl + traderId)
    if (res) {
      setState({
        ...state,
        traderId: traderId,
        traderAccount: res.data
      })
    } else {
      setState({
        ...state,
        traderId: traderId
      })
    }
  }

  useEffect(() => {
    if (routeParams && routeParams.traderId) {
      const traderId = routeParams.traderId
      fetchTrader(traderId)
    }
  }, [])

  const showDepositModal = () => {
    setState({
      ...state,
      isDepositModalVisible: true
    })
  }

  const showWithdrawModal = () => {
    setState({
      ...state,
      isWithdrawModalVisible: true
    })
  }

  const handleDepositCancel = () => {
    setState({
      ...state,
      isDepositModalVisible: false,
      depositFunds: null
    })
    form.resetFields()
  }

  const handleWithdrawCancel = () => {
    setState({
      ...state,
      isWithdrawModalVisible: false,
      withdrawFunds: null
    })
    form.resetFields()
  }

  const handleDepositOk = async () => {
    if (state.depositFunds == null) {
      return
    }

    const traderDepositUrl = depositFundsUrl + state.traderId + ""
        + "/amount/" + state.depositFunds
    const res = await axios.put(traderDepositUrl)
    if (res) {
      const res = await axios.get(traderAccountUrl + state.traderId)
      if (res) {
        form.resetFields()
        setState({
          ...state,
          traderId: state.traderId,
          traderAccount: res.data,
          isDepositModalVisible: false
        })
      } else {
        form.resetFields()
        setState({
          ...state,
          traderId: state.traderId,
          isDepositModalVisible: false
        })
      }
    }
  }

  const handleWithdrawOk = async () => {
    if (state.withdrawFunds == null) {
      return
    }

    const traderDepositUrl = withdrawFundsUrl + state.traderId + ""
        + "/amount/" + state.withdrawFunds
    const res = await axios.put(traderDepositUrl)
    if (res) {
      const res = await axios.get(traderAccountUrl + state.traderId)
      if (res) {
        form.resetFields()
        setState({
          ...state,
          traderId: state.traderId,
          traderAccount: res.data,
          isWithdrawModalVisible: false
        })
      } else {
        form.resetFields()
        setState({
          ...state,
          traderId: state.traderId,
          isWithdrawModalVisible: false
        })
      }
    }
  }

  const onInputChange = (field, value) => {
    setState({
      ...state,
      [field]: value
    })
  }

  return (
      <div className="trader-account-page">
        <NavBar />

        <div className="trader-account-page-content">
          <div className="title">
            Trader Account
          </div>

          <div className="trader-cards">
            <div className="trader-card">
              <div className="info-row">
                <div className="field">

                  <div className="content-heading">
                    First Name
                  </div>
                  <div className="content">
                    { state.traderAccount.trader.firstName }
                  </div>
                </div>
                <div className="field">
                  <div className="content-heading">
                    Last Name
                  </div>
                  <div className="content">
                    { state.traderAccount.trader.lastName }
                  </div>
                </div>
              </div>

              <div className="info-row">
                <div className="field">
                  <div className="content-heading">
                    Email
                  </div>
                  <div className="content">
                    { state.traderAccount.trader.email }
                  </div>
                </div>
                <div className="field">
                  <div className="content-heading">
                    Date of Birth
                  </div>
                  <div className="content">
                    { state.traderAccount.trader.dob }
                  </div>
                </div>
                <div className="field">
                  <div className="content-heading">
                    Country
                  </div>
                  <div className="content">
                    { state.traderAccount.trader.country }
                  </div>
                </div>
              </div>

            </div>
            <div className="trader-card">
              <div className="info-row">
                <div className="field">
                  <div className="content-heading amount">
                    Amount
                  </div>
                  <div className="content amount">
                    { state.traderAccount.account.amount }
                  </div>
                </div>
              </div>
            </div>
            <div className="actions">
              <Button onClick={showDepositModal}>Deposit Funds</Button>
              <Modal title="Deposit Funds" okText="Submit"
                     open={state.isDepositModalVisible}
                     onOk={handleDepositOk} onCancel={handleDepositCancel}>
                <div className="funds-form">
                  <Form form={form}>
                    <div className="funds-field">
                      <Form.Item
                        name="Amount"
                        label="Amount"
                        rules={[{
                          required: true,
                          pattern: new RegExp(/^[0-9]+$/),
                          message: "Amount to deposit has to above 0."
                        }]}
                      >
                        <Input allowClear={false} placeholder="Funds"
                               onChange={(event) => onInputChange(
                                   "depositFunds", event.target.value)} />
                      </Form.Item>
                    </div>
                  </Form>
                </div>

              </Modal>
              <Button onClick={showWithdrawModal}>Withdraw Funds</Button>
              <Modal title="Withdraw Funds" okText="Submit"
                     open={state.isWithdrawModalVisible}
                     onOk={handleWithdrawOk} onCancel={handleWithdrawCancel}>
                <div className="funds-form">
                  <Form form={form}>
                    <div className="funds-field">
                      <Form.Item
                        name="Amount"
                        label="Amount"
                        rules={[{
                          required: true,
                          pattern: new RegExp(/^[0-9]+$/),
                          message: "Amount to withdraw has to above 0."
                        }]}
                      >
                        <Input allowClear={false} placeholder="Funds"
                               onChange={(event) => onInputChange(
                                   "withdrawFunds", event.target.value)} />
                      </Form.Item>
                    </div>
                  </Form>
                </div>
              </Modal>
            </div>
          </div>
        </div>
      </div>
  )
}

export default TraderAccountPage