import React from 'react'

const Deployments = ({deployments: deployments}) => {
    return (
        <div>
            <table className="table table-bordered">
                <thead className="thead-dark">
                <tr>
                    <th scope="col">Namespace</th>
                    <th scope="col">Name</th>
                    <th scope="col">Image</th>
                    <th scope="col">Status</th>
                </tr>
                </thead>

                {deployments.map((deployment) => (
                    <tbody>
                    <tr>
                        <td className="text-center">{deployment.namespace}</td>
                        <td className="text-center">{deployment.name}</td>
                        <td className="text-center">{deployment.image}</td>
                        <td className="text-center">{deployment.status}</td>
                    </tr>
                    </tbody>
                ))}
            </table>
        </div>
    )
};

export default Deployments
