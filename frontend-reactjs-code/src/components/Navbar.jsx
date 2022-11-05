import {
    Link
} from 'react-router-dom';


const Navbar = () => {

    return (
        <div className={'flex space-x-20 mt-10 justify-center'}>
            <Link to={'/'} className={'flex'}>
                <button className={'bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'}>
                    Doctor
                </button>
            </Link>
            <Link to={'/admin'} className={'flex'}>
                <button className={'bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'}>
                    Admin
                </button>
            </Link>
            <Link to={'/patient'} className={'flex'}>
                <button className={'bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'}>
                    Patient
                </button>
            </Link>
        </div>
    )
}

export default Navbar;