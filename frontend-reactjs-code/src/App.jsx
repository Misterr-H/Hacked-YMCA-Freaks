import RightBubble from "./components/RightBubble.jsx";
import LeftBubble from "./components/LeftBubble.jsx";
import {useState} from "react";

const App = () => {
    const [message, setMessage] = useState("");
    const [array, setArray] = useState([]);

    const handleSubmit = async (e) => {
        console.log("submit");
        setMessage("");
        setArray((arr) => [...arr, <RightBubble message={message} />]);
        const response = await fetch("http://161.97.165.160:4000/angela", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({query: {
                message
                }}),
        });
        const data = await response.json();
        setArray((arr) => [...arr, <LeftBubble message={data.replies[0].message}/>]);
    }

    return (
        <>
            <div className={'flex w-full'}>
              <h1 className={'mx-auto text-4xl font-bold mt-10'}>Angela The AI Chatbot</h1>
            </div>
            <div className={'mt-10 mx-auto container p-2 border-2 border-neutral-200 rounded-lg'}>
                {array}
                <div className={'w-full mt-5 border-2 flex border-black rounded-lg'}>
                    <input value={message} onChange={(e) => {
                        setMessage(e.target.value);
                    }} className={'w-full outline-none rounded-lg p-2'}/>
                    <button onClick={handleSubmit} className={'bg-blue-200 px-2 rounded-lg hover:bg-blue-400'}>
                        Send
                    </button>
                </div>
            </div>

        </>
    )
}

export default App;