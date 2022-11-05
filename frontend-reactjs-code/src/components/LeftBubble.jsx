const LeftBubble = ({ message }) => {
    return (
        <div className="bg-blue-200 shadow-xl w-60 mr-auto p-2 rounded-lg text-justify">
            <div className="left-bubble-text">{message}</div>
        </div>
    );
}

export default LeftBubble;