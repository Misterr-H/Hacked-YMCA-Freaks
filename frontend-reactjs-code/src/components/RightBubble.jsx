const RightBubble = ({ message }) => {
    return (
        <div className="bg-gray-200 shadow-xl w-60 ml-auto p-2 rounded-lg text-justify">
            <div className="right-bubble-text">{message}</div>
        </div>
    );
}

export default RightBubble;