 console.log("contacts.js loaded");
 import { Modal } from 'flowbite';


 const viewContactModal=document.getElementById("view_contact_modal");
 // options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: "view_contact_modal",
  override: true
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal() {
    console.log("Opening Contact Modal...");
    contactModal.show();
    // Your modal opening logic here
}
