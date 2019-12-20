import React from 'react';
import Axios from 'axios';

class InputForm extends React.Component {
    state = { namespace: '' };

    handleSubmit = async (event) => {
        event.preventDefault();
        const resp = await Axios.get(`http://localhost:8080/api/k8sclient/v1/namespace/${this.state.namespace}`);
        this.props.onSubmit(resp.data);
        this.setState({ namespace: '' });
    };

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <span className="formtext">Namespace:</span>
                <input
                    type="text"
                    value={this.state.namespace}
                    onChange={event => this.setState({ namespace: event.target.value })}
                    placeholder="Enter Namespace"
                    required
                />
                <button>Get</button>
            </form>
        );
    }
}

export default InputForm
